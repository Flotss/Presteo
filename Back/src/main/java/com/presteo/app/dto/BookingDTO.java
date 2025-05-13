package com.presteo.app.dto;

import com.presteo.app.model.Booking;
import com.presteo.app.model.BookingStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private ServiceDTO service;
    private UserDTO customer;
    private String additionalInfo;
    private String address;
    private Date bookingDate;
    private BookingStatusType status;
    private String createdAt;
    private String updatedAt;

    public static BookingDTO build(Booking booking) {
        return builder()
                .id(booking.getId())
                .service(ServiceDTO.build(booking.getService()))
                .customer(UserDTO.build(booking.getCustomer()))
                .additionalInfo(booking.getAdditionalInfo())
                .address(booking.getAddress())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
