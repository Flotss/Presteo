package com.presteo.app.controller;


import com.presteo.app.controller.model.ServiceForm;
import com.presteo.app.dto.ServiceDTO;
import com.presteo.app.model.RoleType;
import com.presteo.app.model.Service;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.security.utils.SecurityUtils;
import com.presteo.app.service.ServiceService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices().stream().map(ServiceDTO::build).toList());
    }

    @GetMapping("provider/{providerId}")
    public ResponseEntity<List<ServiceDTO>> getServiceByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(serviceService.getServiceByProviderId(providerId).stream().map(ServiceDTO::build).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
        var service = serviceService.getServiceById(id);
        return ResponseEntity.ok(ServiceDTO.build(service));
    }

    @SecuredRoute(roles = {RoleType.PROVIDER})
    @PostMapping("/create")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceForm service) {
        SecurityUtils.verifyOwnershipOrAdmin(x -> x.getProviderId(), service);

        var createdService = serviceService.createService(service);
        return ResponseEntity.ok(ServiceDTO.build(createdService));
    }

    @SecuredRoute(roles = {RoleType.PROVIDER})
    @PutMapping("/update")
    public ResponseEntity<ServiceDTO> updateService(@RequestBody Service service) {
        checkCredential(service);

        var updatedService = serviceService.updateService(service);
        return ResponseEntity.ok(ServiceDTO.build(updatedService));
    }

    @SecuredRoute(roles = {RoleType.PROVIDER})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        checkCredential(id);

        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @SecuredRoute(roles = {RoleType.PROVIDER})
    @PostMapping("/update-service-picture")
    public ResponseEntity<Void> updateProfilePicture(
            @Parameter(description = "ID of the user to update") @RequestParam Long id,
            @Parameter(description = "New profile picture") @RequestParam("file") MultipartFile file) {
        checkCredential(id);
        return serviceService.updateServicePicture(id, file)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private void checkCredential(Service service) {
        SecurityUtils.verifyOwnershipOrAdmin(x -> x.getProvider().getId(), service);
    }

    private void checkCredential(Long serviceId) {
        var service = serviceService.getServiceById(serviceId);
        checkCredential(service);
    }
}
