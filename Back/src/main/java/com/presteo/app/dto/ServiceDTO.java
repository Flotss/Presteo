package com.presteo.app.dto;

import com.presteo.app.model.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Long id;
    private UserDTO provider;
    private String title;
    private String description;
    private int durationHours;
    private String city;
    private String domain;
    private String imageUrl;
    private Double price;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private Double averageRating;
    private Long reviewCount;
    private List<ReviewDTO> reviews;

    public static ServiceDTO build(Service service) {
        return builder()
                .id(service.getId())
                .provider(UserDTO.build(service.getProvider()))
                .title(service.getTitle())
                .description(service.getDescription())
                .durationHours(service.getDurationHours())
                .city(service.getCity())
                .domain(service.getDomain())
                .imageUrl(service.getImageUrl())
                .price(service.getPrice())
                .isActive(service.isActive())
                .averageRating(service.getAverageRating())
                .reviewCount(service.getReviewCount())
                .reviews(service.getReviews() != null ? service.getReviews().stream().map(ReviewDTO::buildMini).toList() : List.of())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }
}
