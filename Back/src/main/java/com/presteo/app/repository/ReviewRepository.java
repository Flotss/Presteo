package com.presteo.app.repository;

import com.presteo.app.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByServiceId(Long serviceId);
    List<Review> findAllByCustomerId(Long customerId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.service.id = ?1")
    Double getAverageRatingByServiceId(Long serviceId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.service.provider.id = ?1")
    Double getAverageRatingByProviderId(Long providerId);
    
    boolean existsByServiceIdAndCustomerId(Long serviceId, Long customerId);

    List<Review> findAllByService_Provider_Id(Long providerId);
} 