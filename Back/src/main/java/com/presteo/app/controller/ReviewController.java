package com.presteo.app.controller;

import com.presteo.app.dto.ReviewDTO;
import com.presteo.app.model.RoleType;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.service.ReviewService;
import com.presteo.app.controller.model.ReviewForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "API for managing service reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create a review", description = "Creates a new review for a service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid review data"),
        @ApiResponse(responseCode = "404", description = "Service or customer not found")
    })

    @SecuredRoute(roles = {RoleType.CUSTOMER})
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @RequestBody ReviewForm form) {
        var review = reviewService.createReview(form.getServiceId(), form.getCustomerId(), form.getRating(), form.getReviewText());
        return ResponseEntity.ok(ReviewDTO.build(review));
    }

    @Operation(summary = "Get service reviews", description = "Retrieves all reviews for a specific service")
    @ApiResponse(responseCode = "200", description = "List of reviews successfully retrieved")
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ReviewDTO>> getServiceReviews(
            @Parameter(description = "Service ID") @PathVariable Long serviceId) {
        var reviews = reviewService.getServiceReviews(serviceId);
        return ResponseEntity.ok(reviews.stream().map(ReviewDTO::build).toList());
    }

    @Operation(summary = "Get customer reviews", description = "Retrieves all reviews made by a specific customer")
    @ApiResponse(responseCode = "200", description = "List of reviews successfully retrieved")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewDTO>> getCustomerReviews(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        var reviews = reviewService.getCustomerReviews(customerId);
        return ResponseEntity.ok(reviews.stream().map(ReviewDTO::build).toList());
    }

    @Operation(summary = "Get service average rating", description = "Retrieves the average rating for a specific service")
    @ApiResponse(responseCode = "200", description = "Average rating successfully retrieved")
    @GetMapping("/service/{serviceId}/average")
    public ResponseEntity<Double> getServiceAverageRating(
            @Parameter(description = "Service ID") @PathVariable Long serviceId) {
        var averageRating = reviewService.getServiceAverageRating(serviceId);
        return ResponseEntity.ok(averageRating);
    }

    @Operation(summary = "Delete a review", description = "Deletes a specific review")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @SecuredRoute(roles = {RoleType.CUSTOMER, RoleType.ADMIN})
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID") @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
} 