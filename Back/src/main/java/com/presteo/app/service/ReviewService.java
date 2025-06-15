package com.presteo.app.service;

import com.presteo.app.model.Review;
import com.presteo.app.model.Service;
import com.presteo.app.model.User;
import com.presteo.app.repository.ReviewRepository;
import com.presteo.app.repository.ServiceRepository;
import com.presteo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, ServiceRepository serviceRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public Review createReview(Long serviceId, Long customerId, Integer rating, String reviewText) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (reviewRepository.existsByServiceIdAndCustomerId(serviceId, customerId)) {
            throw new IllegalStateException("Customer has already reviewed this service");
        }

        Review review = new Review();
        review.setService(service);
        review.setCustomer(customer);
        review.setRating(rating);
        review.setReviewText(reviewText);

        return reviewRepository.save(review);
    }

    public List<Review> getServiceReviews(Long serviceId) {
        return reviewRepository.findAllByServiceId(serviceId);
    }

    public List<Review> getCustomerReviews(Long customerId) {
        return reviewRepository.findAllByCustomerId(customerId);
    }

    public List<Review> getReviewsForProvider(Long providerId) {
        return reviewRepository.findAllByService_Provider_Id(providerId);
    }

    public Double getServiceAverageRating(Long serviceId) {
        return reviewRepository.getAverageRatingByServiceId(serviceId);
    }

    public Double getProviderAverageRating(Long providerId) {
        return reviewRepository.getAverageRatingByProviderId(providerId);
    }

    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }
} 