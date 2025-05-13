package com.presteo.app.service;

import com.presteo.app.controller.model.CreateBooking;
import com.presteo.app.dto.BookingDTO;
import com.presteo.app.model.Booking;
import com.presteo.app.model.BookingStatusType;
import com.presteo.app.model.Service;
import com.presteo.app.model.User;
import com.presteo.app.repository.BookingRepository;
import com.presteo.app.repository.ServiceRepository;
import com.presteo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final int TRAVEL_BUFFER_MINUTES = 25;
    private static final int AVAILABILITY_SLOT_MINUTES = 30;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          ServiceRepository serviceRepository,
                          UserRepository userRepository,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with ID " + id + " not found"));
    }

    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findAllByCustomer_Id(customerId);
    }

    public List<Booking> getBookingsByProviderId(Long providerId) {
        return bookingRepository.findAllByService_Provider_Id(providerId);
    }

    public Booking createBooking(CreateBooking createBooking) {
        Service service = serviceRepository.findById(createBooking.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service with ID " + createBooking.getServiceId() + " not found"));

        User customer = userRepository.findById(createBooking.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with ID " + createBooking.getCustomerId() + " not found"));

        if (!isProviderAvailable(service.getProvider().getId(), createBooking.getBookingDate(), service.getDurationHours())) {
            throw new IllegalStateException("Provider is not available at the requested time");
        }

        Booking booking = new Booking();
        booking.setService(service);
        booking.setCustomer(customer);
        booking.setAdditionalInfo(createBooking.getAdditionalInfo());
        booking.setAddress(createBooking.getAddress());
        booking.setBookingDate(createBooking.getBookingDate());
        booking.setStatus(BookingStatusType.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        sendBookingStatusChangeEmails(savedBooking);

        return savedBooking;
    }

    public Booking updateBookingStatus(Long bookingId, BookingStatusType newStatus) {
        Booking booking = getBookingById(bookingId);
        BookingStatusType oldStatus = booking.getStatus();

        // Check if the status change is valid
        if (!isValidStatusTransition(oldStatus, newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + oldStatus + " to " + newStatus);
        }

        booking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(booking);

        // Send notification emails about status change
        sendBookingStatusChangeEmails(updatedBooking);

        return updatedBooking;
    }

    public Booking updateBooking(Booking booking) {
        if (!bookingRepository.existsById(booking.getId())) {
            throw new EntityNotFoundException("Booking with ID " + booking.getId() + " not found");
        }
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking with ID " + id + " not found");
        }
        bookingRepository.deleteById(id);
    }


    /**
     * Checks if the provider is available for a booking at the specified time with the given duration.
     * Takes into account travel buffer time before and after the service.
     */
    public boolean isProviderAvailable(Long serviceId, Date requestedDate, int serviceDurationHours) {
        // Convert requestedDate to LocalDateTime for easier manipulation
        LocalDateTime requestedDateTime = requestedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Calculate service start and end times with travel buffer
        LocalDateTime serviceStartTime = requestedDateTime.minusMinutes(TRAVEL_BUFFER_MINUTES);
        LocalDateTime serviceEndTime = requestedDateTime
                .plusHours(serviceDurationHours)
                .plusMinutes(TRAVEL_BUFFER_MINUTES);

        // Get all bookings for this provider on the same day
        LocalDate bookingDate = requestedDateTime.toLocalDate();
        List<Booking> providerBookings = getBookingsByProviderId(serviceId);

        // Filter bookings that are on the same day and are confirmed or in progress
        List<Booking> relevantBookings = providerBookings.stream()
                .filter(booking -> {
                    LocalDateTime bookingDateTime = booking.getBookingDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    BookingStatusType status = booking.getStatus();
                    return bookingDateTime.toLocalDate().equals(bookingDate) &&
                            (status == BookingStatusType.CONFIRMED ||
                                    status== BookingStatusType.IN_PROGRESS);
                })
                .collect(Collectors.toList());

        // Check for overlaps with existing bookings
        for (Booking booking : relevantBookings) {
            LocalDateTime existingBookingStart = booking.getBookingDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .minusMinutes(TRAVEL_BUFFER_MINUTES);

            LocalDateTime existingBookingEnd = booking.getBookingDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .plusHours(booking.getService().getDurationHours())
                    .plusMinutes(TRAVEL_BUFFER_MINUTES);

            if (!(serviceEndTime.isBefore(existingBookingStart) || serviceStartTime.isAfter(existingBookingEnd))) {
                return false; // There is an overlap, provider is not available
            }
        }

        return true;
    }

    /**
     * Gets the availability of a provider for a specific date.
     * Returns a list of time slots every 30 minutes with availability status.
     */
    public List<Map<String, Object>> getProviderAvailability(Long providerId, LocalDate date) {
        List<Map<String, Object>> availabilitySlots = new ArrayList<>();

        // Define working hours (8:00 AM to 8:00 PM)
        LocalDateTime startOfDay = date.atTime(8, 0);
        LocalDateTime endOfDay = date.atTime(20, 0);

        // Get all bookings for this provider on the specified date
        List<Booking> providerBookings = getBookingsByProviderId(providerId);
        List<Booking> relevantBookings = providerBookings.stream()
                .filter(booking -> {
                    LocalDateTime bookingDateTime = booking.getBookingDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    return bookingDateTime.toLocalDate().equals(date) &&
                            (booking.getStatus() == BookingStatusType.CONFIRMED ||
                                    booking.getStatus() == BookingStatusType.IN_PROGRESS);
                })
                .collect(Collectors.toList());

        // Create time slots every 30 minutes
        LocalDateTime currentSlot = startOfDay;
        while (currentSlot.isBefore(endOfDay)) {
            LocalDateTime slotEndTime = currentSlot.plusMinutes(AVAILABILITY_SLOT_MINUTES);
            boolean isAvailable = true;

            // Check if this slot overlaps with any booking
            for (Booking booking : relevantBookings) {
                LocalDateTime bookingStart = booking.getBookingDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .minusMinutes(TRAVEL_BUFFER_MINUTES);

                LocalDateTime bookingEnd = booking.getBookingDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .plusHours(booking.getService().getDurationHours())
                        .plusMinutes(TRAVEL_BUFFER_MINUTES);

                if (!(slotEndTime.isBefore(bookingStart) || currentSlot.isAfter(bookingEnd))) {
                    isAvailable = false;
                    break;
                }
            }

            // Add slot to the list
            Map<String, Object> slot = new HashMap<>();
            slot.put("time", currentSlot);
            slot.put("available", isAvailable);
            availabilitySlots.add(slot);

            // Move to next slot
            currentSlot = slotEndTime;
        }

        return availabilitySlots;
    }

    /**
     * Checks if the status transition is valid.
     */
    private boolean isValidStatusTransition(BookingStatusType oldStatus, BookingStatusType newStatus) {
        return switch (oldStatus) {
            case PENDING -> newStatus == BookingStatusType.CONFIRMED || newStatus == BookingStatusType.CANCELLED;
            case CONFIRMED -> newStatus == BookingStatusType.IN_PROGRESS || newStatus == BookingStatusType.CANCELLED;
            case IN_PROGRESS -> newStatus == BookingStatusType.COMPLETED;
            case COMPLETED, CANCELLED -> false; // Terminal states, no further transitions allowed
            default -> false;
        };
    }

    /**
     * Sends notification emails to the customer and provider about booking status changes.
     */
    private void sendBookingStatusChangeEmails(Booking booking) {
        User customer = booking.getCustomer();
        User provider = booking.getService().getProvider();

        String customerSubject = "Update on your booking #" + booking.getId();
        String providerSubject = "Update on booking #" + booking.getId();

        String customerEmail = getCustomerEmailContent(booking);
        String providerEmail = getProviderEmailContent(booking);

        // Send emails to both parties
        emailService.sendEmail(customer.getEmail(), customerSubject, customerEmail);
        emailService.sendEmail(provider.getEmail(), providerSubject, providerEmail);
    }

    /**
     * Generates email content for customer notifications.
     */
    private String getCustomerEmailContent(Booking booking) {
        String statusMessage = getStatusMessage(booking.getStatus(), true);
        Service service = booking.getService();

        return "<html><body>" +
                "<h2>Booking Update</h2>" +
                "<p>Hello " + booking.getCustomer().getFirstName() + ",</p>" +
                "<p>" + statusMessage + "</p>" +
                "<p><strong>Booking Details:</strong></p>" +
                "<ul>" +
                "  <li>Service: " + service.getTitle() + "</li>" +
                "  <li>Provider: " + service.getProvider().getFirstName() + " " + service.getProvider().getLastName() + "</li>" +
                "  <li>Date and Time: " + booking.getBookingDate() + "</li>" +
                "  <li>Address: " + booking.getAddress() + "</li>" +
                "  <li>Duration: " + service.getDurationHours() + " hours</li>" +
                "  <li>Price: $" + service.getPrice() + "</li>" +
                "</ul>" +
                "<p>Thank you for using our services!</p>" +
                "<p>Best regards,<br/>Presteo Team</p>" +
                "</body></html>";
    }

    /**
     * Generates email content for provider notifications.
     */
    private String getProviderEmailContent(Booking booking) {
        String statusMessage = getStatusMessage(booking.getStatus(), false);
        Service service = booking.getService();
        User customer = booking.getCustomer();

        return "<html><body>" +
                "<h2>Booking Update</h2>" +
                "<p>Hello " + service.getProvider().getFirstName() + ",</p>" +
                "<p>" + statusMessage + "</p>" +
                "<p><strong>Booking Details:</strong></p>" +
                "<ul>" +
                "  <li>Service: " + service.getTitle() + "</li>" +
                "  <li>Customer: " + customer.getFirstName() + " " + customer.getLastName() + "</li>" +
                "  <li>Date and Time: " + booking.getBookingDate() + "</li>"
                + "  <li>Address: " + booking.getAddress() + "</li>" +
                "  <li>Duration: " + service.getDurationHours() + " hours</li>" +
                "  <li>Price: $" + service.getPrice() + "</li>" +
                "</ul>" +
                "<p>Thank you for using our services!</p>" +
                "<p>Best regards,<br/>Presteo Team</p>" +
                "</body></html>";
    }

    /**
     * Returns a message based on the booking status.
     */
    private String getStatusMessage(BookingStatusType status, boolean isCustomer) {
        return switch (status) {
            case PENDING -> isCustomer ? "Your booking is pending confirmation." : "A new booking has been created and is pending confirmation.";
            case CONFIRMED -> isCustomer ? "Your booking has been confirmed." : "The booking has been confirmed.";
            case IN_PROGRESS -> isCustomer ? "Your booking is currently in progress." : "The booking is currently in progress.";
            case COMPLETED -> isCustomer ? "Your booking has been completed." : "The booking has been completed.";
            case CANCELLED -> isCustomer ? "Your booking has been cancelled." : "The booking has been cancelled.";
            default -> "";
        };
    }
}
