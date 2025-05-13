package com.presteo.app.controller;

import com.presteo.app.controller.model.CreateBooking;
import com.presteo.app.dto.BookingDTO;
import com.presteo.app.model.Booking;
import com.presteo.app.model.BookingStatusType;
import com.presteo.app.model.RoleType;
import com.presteo.app.model.Service;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.security.utils.SecurityUtils;
import com.presteo.app.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing bookings between customers and service providers.
 * Handles creation, modification, deletion, and consultation of bookings as well as
 * checking provider availability.
 */
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "API for managing service bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Retrieves all bookings in the system.
     *
     * @return List of all bookings
     */
    @Operation(summary = "Get all bookings", description = "Retrieves the complete list of bookings in the system")
    @ApiResponse(responseCode = "200", description = "List of bookings successfully retrieved")
    @SecuredRoute(roles = {RoleType.ADMIN})
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    /**
     * Retrieves a specific booking by its identifier.
     *
     * @param id Booking identifier
     * @return The corresponding booking
     * @throws ResponseStatusException If the booking is not found (404)
     */
    @Operation(summary = "Get booking by ID", description = "Retrieves the details of a specific booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking found"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(
            @Parameter(description = "ID of the booking to retrieve") 
            @PathVariable Long id) {
        checkCredential(id);
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(BookingDTO.build(booking));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves all bookings for a specific customer.
     *
     * @param customerId Customer identifier
     * @return List of customer's bookings
     */
    @Operation(summary = "Get customer bookings", description = "Retrieves the list of bookings made by a specific customer")
    @ApiResponse(responseCode = "200", description = "List of customer bookings successfully retrieved")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomerId(
            @Parameter(description = "ID of the customer whose bookings to retrieve") 
            @PathVariable Long customerId) {
        var customer = bookingService.getBookingsByCustomerId(customerId);
        return ResponseEntity.ok(customer.stream().map(BookingDTO::build).toList());
    }

    /**
     * Retrieves all bookings for a specific service provider.
     *
     * @param providerId Provider identifier
     * @return List of provider's bookings
     */
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @Operation(summary = "Get provider bookings", description = "Retrieves the list of bookings addressed to a specific service provider")
    @ApiResponse(responseCode = "200", description = "List of provider bookings successfully retrieved")
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByProviderId(
            @Parameter(description = "ID of the provider whose bookings to retrieve") 
            @PathVariable Long providerId) {
        var provider = bookingService.getBookingsByProviderId(providerId);
        return ResponseEntity.ok(provider.stream().map(BookingDTO::build).toList());
    }


    /**
     * Retrieves all bookings from the authenticated customer.
     *
     * @return List of customer's bookings
     */
    @Operation(summary = "Get my bookings", description = "Retrieves the list of bookings made by the authenticated customer")
    @ApiResponse(responseCode = "200", description = "List of customer bookings successfully retrieved")
    @GetMapping("/mybookings")
    public ResponseEntity<List<BookingDTO>> getMyBookings() {
        var customer = bookingService.getBookingsByCustomerId(SecurityUtils.getAuthenticatedUser().getId());
        return ResponseEntity.ok(customer.stream().map(BookingDTO::build).toList());
    }

    /**
     * Creates a new booking in the system.
     *
     * @param booking Data for the booking to create
     * @return The created booking
     * @throws ResponseStatusException If data is invalid (400) or if a referenced entity does not exist (404)
     */
    @SecuredRoute()
    @Operation(summary = "Create a booking", description = "Registers a new booking for a service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Booking created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid booking data"),
        @ApiResponse(responseCode = "404", description = "Service or user not found")
    })
    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(
            @Parameter(description = "Data for the new booking") 
            @RequestBody CreateBooking booking) {
        try {
            Booking bookingCreated = bookingService.createBooking(booking);
            return new ResponseEntity<>(BookingDTO.build(bookingCreated), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates the status of an existing booking.
     *
     * @param id Booking identifier
     * @param status New booking status
     * @return The updated booking
     * @throws ResponseStatusException If the booking is not found (404) or if the status change is invalid (400)
     */
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @Operation(summary = "Update booking status", description = "Changes the status of a booking (pending, confirmed, canceled, etc.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status change"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @Parameter(description = "ID of the booking to update") 
            @PathVariable Long id,
            @Parameter(description = "New booking status") 
            @RequestParam BookingStatusType status) {
        try {
            Booking booking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(BookingDTO.build(booking));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates information for an existing booking.
     *
     * @param id Booking identifier
     * @param booking New booking data
     * @return The updated booking
     * @throws ResponseStatusException If the booking is not found (404)
     */
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @Operation(summary = "Update a booking", description = "Modifies information for an existing booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(
            @Parameter(description = "ID of the booking to update") 
            @PathVariable Long id,
            @Parameter(description = "New booking data") 
            @RequestBody Booking booking) {
        try {
            booking.setId(id);
            var updatedBooking = bookingService.updateBooking(booking);
            return ResponseEntity.ok(BookingDTO.build(updatedBooking));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Deletes a booking from the system.
     *
     * @param id Identifier of the booking to delete
     * @return Empty response with code 204 in case of success
     * @throws ResponseStatusException If the booking is not found (404)
     */
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @Operation(summary = "Delete a booking", description = "Permanently removes a booking from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @Parameter(description = "ID of the booking to delete") 
            @PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Checks if a provider is available at a specific date and for a specific duration.
     *
     * @param providerId Provider identifier
     * @param requestedDate Requested date
     * @param serviceDurationHours Service duration in hours
     * @return true if the provider is available, false otherwise
     */
    @SecuredRoute()
    @Operation(summary = "Check provider availability", description = "Checks if a provider is available at a specific date and for a specific duration")
    @ApiResponse(responseCode = "200", description = "Availability checked successfully")
    @GetMapping("/provider/{providerId}/available")
    public ResponseEntity<Boolean> isProviderAvailable(
            @Parameter(description = "ID of the provider to check") 
            @PathVariable Long providerId,
            @Parameter(description = "Requested date and time for the booking") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date requestedDate,
            @Parameter(description = "Service duration in hours") 
            @RequestParam int serviceDurationHours) {
        return ResponseEntity.ok(
                bookingService.isProviderAvailable(providerId, requestedDate, serviceDurationHours));
    }

    /**
     * Retrieves availability slots for a provider on a specific date.
     *
     * @param providerId Provider identifier
     * @param date Date to check availabilities for
     * @return List of available and occupied slots
     */
    @SecuredRoute()
    @Operation(summary = "Get provider availability", description = "Retrieves the list of available slots for a provider on a specific day")
    @ApiResponse(responseCode = "200", description = "Availabilities retrieved successfully")
    @GetMapping("/provider/{providerId}/availability")
    public ResponseEntity<List<Map<String, Object>>> getProviderAvailability(
            @Parameter(description = "Provider ID") 
            @PathVariable Long providerId,
            @Parameter(description = "Date to check availabilities for") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bookingService.getProviderAvailability(providerId, date));
    }


    /**
     * Checks if the current user is authorized to access the service.
     *
     * @param booking Booking to check
     */
    private void checkCredential(Booking booking) {
        SecurityUtils.verifyOwnershipOrAdmin(
                booking,
                x -> x.getCustomer().getId(),
                x -> x.getService().getProvider().getId());
    }

    /**
     * Checks if the current user is authorized to access the service by its ID.
     *
     * @param bookingId ID of the service to check
     */
    private void checkCredential(Long bookingId) {
        var service = bookingService.getBookingById(bookingId);
        checkCredential(service);
    }
}
