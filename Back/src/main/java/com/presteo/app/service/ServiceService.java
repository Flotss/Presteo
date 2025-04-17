package com.presteo.app.service;

import com.presteo.app.model.Service;
import com.presteo.app.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
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

    public Service createService(Service service) {
        return serviceRepository.save(service);
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
}
