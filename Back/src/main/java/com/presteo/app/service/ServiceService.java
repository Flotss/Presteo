package com.presteo.app.service;

import com.google.cloud.storage.StorageException;
import com.presteo.app.controller.model.ServiceForm;
import com.presteo.app.model.Service;
import com.presteo.app.model.User;
import com.presteo.app.repository.ServiceRepository;
import com.presteo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;
    private final ReviewService reviewService;

    public ServiceService(ServiceRepository serviceRepository, UserRepository userRepository, ImageStorageService imageStorageService, ReviewService reviewService) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.imageStorageService = imageStorageService;
        this.reviewService = reviewService;
    }

    public List<Service> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        services.forEach(service -> {
            service.setAverageRating(reviewService.getServiceAverageRating(service.getId()));
            service.setReviewCount((long) reviewService.getServiceReviews(service.getId()).size());
        });
        return services;
    }

    public List<Service> getServiceByProviderId(Long providerId) {
        List<Service> services = serviceRepository.findAllByProvider_Id(providerId);
        services.forEach(service -> {
            service.setAverageRating(reviewService.getServiceAverageRating(service.getId()));
            service.setReviewCount((long) reviewService.getServiceReviews(service.getId()).size());
        });
        return services;
    }

    public Service getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service with ID " + id + " not found"));
        service.setAverageRating(reviewService.getServiceAverageRating(id));
        service.setReviewCount((long) reviewService.getServiceReviews(id).size());
        service.getProvider().setAverageRating(reviewService.getProviderAverageRating(service.getProvider().getId()));
        var reviews = reviewService.getReviewsForProvider(service.getProvider().getId());
        service.getProvider().setReviewCount((reviews != null) ? (long) reviews.size() : 0L);
        service.setReviews(reviews);
        return service;
    }

    public Service createService(ServiceForm service) {
        User provider = userRepository.findById(service.getProviderId())
                .orElseThrow(() -> new EntityNotFoundException("Provider with ID " + service.getProviderId() + " not found"));

        String imageUrl = null;
        if (service.getImageFile() != null && !service.getImageFile().isEmpty()) {
            try {
                imageUrl = imageStorageService.uploadServiceImage(service.getImageFile());
            } catch (IOException | StorageException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        Service newService = new Service();
        newService.setProvider(provider);
        newService.setTitle(service.getTitle());
        newService.setDescription(service.getDescription());
        newService.setCity(service.getCity());
        newService.setPrice(service.getPrice());
        newService.setDurationHours(service.getDurationHours());
        newService.setDomain(service.getDomain());
        newService.setImageUrl(imageUrl);
        return serviceRepository.save(newService);
    }

    public Service updateService(Service service) {
        var existingService = serviceRepository.findById(service.getId())
                .orElseThrow(() -> new EntityNotFoundException("Service with ID " + service.getId() + " not found"));

        existingService.setTitle(service.getTitle());
        existingService.setDescription(service.getDescription());
        existingService.setCity(service.getCity());
        existingService.setPrice(service.getPrice());
        existingService.setDurationHours(service.getDurationHours());
        existingService.setDomain(service.getDomain());

        return serviceRepository.save(existingService);
    }

    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Service with ID " + id + " not found");
        }
        serviceRepository.deleteById(id);
    }

    public Service updateServicePicture(Long id, MultipartFile file) {
        return serviceRepository.findById(id).map(service -> {
            String url;
            try {
                url = imageStorageService.uploadServiceImage(file);
            } catch (IOException | StorageException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
            service.setImageUrl(url);
            serviceRepository.save(service);
            return service;
        }).orElseThrow(() -> new EntityNotFoundException("Service not found"));
    }
}
