package com.presteo.app.dto;

import com.presteo.app.model.Booking;
import com.presteo.app.model.BookingStatusType;
import com.presteo.app.model.Review;
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
    private Double finalPrice;
    private BookingStatusType status;
    private Date createdAt;
    private Date updatedAt;
    private Integer userRating;
    private String userReviewText;
    private Boolean hasReview;

    public static BookingDTO build(Booking booking) {
        return builder()
                .id(booking.getId())
                .service(ServiceDTO.build(booking.getService()))
                .customer(UserDTO.build(booking.getCustomer()))
                .additionalInfo(booking.getAdditionalInfo())
                .address(booking.getAddress())
                .bookingDate(booking.getBookingDate())
                .finalPrice(booking.getFinalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    public static BookingDTO build(Booking booking, Review review) {
        return builder()
                .id(booking.getId())
                .service(ServiceDTO.build(booking.getService()))
                .customer(UserDTO.build(booking.getCustomer()))
                .additionalInfo(booking.getAdditionalInfo())
                .address(booking.getAddress())
                .bookingDate(booking.getBookingDate())
                .finalPrice(booking.getFinalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .userRating(review != null ? review.getRating() : null)
                .userReviewText(review != null ? review.getReviewText() : null)
                .hasReview(review != null)
                .build();
    }
}
