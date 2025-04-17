package com.presteo.app.controller;


import com.presteo.app.model.Service;
import com.presteo.app.repository.ServiceRepository;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@SecuredRoute
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("provider/{providerId}")
    public ResponseEntity<List<Service>> getServiceByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(serviceService.getServiceByProviderId(providerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.ok( serviceService.createService(service));
    }

    @PutMapping("/update")
    public ResponseEntity<Service> updateService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceService.updateService(service));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
