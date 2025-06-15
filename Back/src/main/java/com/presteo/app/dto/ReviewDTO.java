package com.presteo.app.dto;

import com.presteo.app.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long serviceId;
    private UserDTO customer;
    private Integer rating;
    private String reviewText;
    private Date createdAt;
    private Date updatedAt;

    public static ReviewDTO build(Review review) {
        return builder()
                .id(review.getId())
                .serviceId(review.getService().getId())
                .customer(UserDTO.build(review.getCustomer()))
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public static ReviewDTO buildMini(Review review) {
        return builder()
                .customer(UserDTO.build(review.getCustomer()))
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .build();
    }
} 