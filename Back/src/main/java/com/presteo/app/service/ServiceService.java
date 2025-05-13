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

    public ServiceService(ServiceRepository serviceRepository, UserRepository userRepository, ImageStorageService imageStorageService) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.imageStorageService = imageStorageService;
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<Service> getServiceByProviderId(Long providerId) {
        return serviceRepository.findAllByProvider_Id(providerId);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service with ID " + id + " not found"));
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
        newService.setPrice(service.getPrice());
        newService.setDomain(service.getDomain());
        newService.setImageUrl(imageUrl);
        return serviceRepository.save(newService);
    }

    public Service updateService(Service service) {
        if (!serviceRepository.existsById(service.getId())) {
            throw new EntityNotFoundException("Service with ID " + service.getId() + " not found");
        }
        return serviceRepository.save(service);
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
