package com.presteo.app.controller;


import com.presteo.app.controller.model.ServiceForm;
import com.presteo.app.dto.ServiceDTO;
import com.presteo.app.model.RoleType;
import com.presteo.app.model.Service;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.security.utils.SecurityUtils;
import com.presteo.app.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller responsible for managing services offered by providers.
 * Allows creating, viewing, modifying, and deleting services, as well as
 * managing images associated with services.
 */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "API for managing services offered by providers")
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * Retrieves all services available in the system.
     *
     * @return List of all services as DTOs
     */
    @Operation(summary = "Get all services", description = "Retrieves the complete list of available services")
    @ApiResponse(responseCode = "200", description = "List of services successfully retrieved")
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices().stream().map(ServiceDTO::build).toList());
    }

    /**
     * Retrieves all services offered by a specific provider.
     *
     * @param providerId Provider identifier
     * @return List of provider's services as DTOs
     */
    @Operation(summary = "Get provider services", description = "Retrieves the list of services offered by a specific provider")
    @ApiResponse(responseCode = "200", description = "List of provider services successfully retrieved")
    @GetMapping("provider/{providerId}")
    public ResponseEntity<List<ServiceDTO>> getServiceByProviderId(
            @Parameter(description = "ID of the provider whose services to retrieve") 
            @PathVariable Long providerId) {
        return ResponseEntity.ok(serviceService.getServiceByProviderId(providerId).stream().map(ServiceDTO::build).toList());
    }

    /**
     * Retrieves a specific service by its identifier.
     *
     * @param id Service identifier
     * @return The corresponding service as a DTO
     */
    @Operation(summary = "Get service by ID", description = "Retrieves the details of a specific service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service found and successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(
            @Parameter(description = "ID of the service to retrieve") 
            @PathVariable Long id) {
        var service = serviceService.getServiceById(id);
        return ResponseEntity.ok(ServiceDTO.build(service));
    }

    /**
     * Creates a new service in the system.
     * Accessible only to users with the PROVIDER role.
     *
     * @param service Data for the service to create
     * @return The created service as a DTO
     */
    @Operation(summary = "Create a service", description = "Registers a new service offered by a provider")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid service data"),
        @ApiResponse(responseCode = "403", description = "User not authorized to create this service")
    })
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @PostMapping("/create")
    public ResponseEntity<ServiceDTO> createService(
            @Parameter(description = "Data for the new service") 
            @RequestBody ServiceForm service) {
        SecurityUtils.verifyOwnershipOrAdmin(service, ServiceForm::getProviderId);

        var createdService = serviceService.createService(service);
        return ResponseEntity.ok(ServiceDTO.build(createdService));
    }

    /**
     * Updates information for an existing service.
     * Accessible only to users with the PROVIDER role who own the service.
     *
     * @param service New service data
     * @return The updated service as a DTO
     */
    @Operation(summary = "Update a service", description = "Modifies information for an existing service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service updated successfully"),
        @ApiResponse(responseCode = "403", description = "User not authorized to modify this service"),
        @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @PutMapping("/update")
    public ResponseEntity<ServiceDTO> updateService(
            @Parameter(description = "Updated service data") 
            @RequestBody Service service) {
        checkCredential(service);

        var updatedService = serviceService.updateService(service);
        return ResponseEntity.ok(ServiceDTO.build(updatedService));
    }

    /**
     * Deletes a service from the system.
     * Accessible only to users with the PROVIDER role who own the service.
     *
     * @param id Identifier of the service to delete
     * @return Empty response with code 204 in case of success
     */
    @Operation(summary = "Delete a service", description = "Permanently removes a service from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Service deleted successfully"),
        @ApiResponse(responseCode = "403", description = "User not authorized to delete this service"),
        @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(
            @Parameter(description = "ID of the service to delete") 
            @PathVariable Long id) {
        checkCredential(id);

        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the image associated with a service.
     * Accessible only to users with the PROVIDER role who own the service.
     *
     * @param id Service identifier
     * @param file New service image
     * @return The updated service as a DTO
     */
    @Operation(summary = "Update service image", description = "Changes the image associated with an existing service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service image updated successfully"),
        @ApiResponse(responseCode = "403", description = "User not authorized to modify this service"),
        @ApiResponse(responseCode = "404", description = "Service not found"),
        @ApiResponse(responseCode = "400", description = "Invalid image file")
    })
    @SecuredRoute(roles = {RoleType.PROVIDER})
    @PostMapping("/update-service-picture")
    public ResponseEntity<ServiceDTO> updateServicePicture(
            @Parameter(description = "ID of the service to update") @RequestParam Long id,
            @Parameter(description = "New service image") @RequestParam("file") MultipartFile file) {
        checkCredential(id);

        Service updatedService = serviceService.updateServicePicture(id, file);
        return ResponseEntity.ok(ServiceDTO.build(updatedService));
    }

    /**
     * Checks if the current user is authorized to access the service.
     *
     * @param service Service to check
     */
    private void checkCredential(Service service) {
        SecurityUtils.verifyOwnershipOrAdmin(service, x -> x.getProvider().getId());
    }

    /**
     * Checks if the current user is authorized to access the service by its ID.
     *
     * @param serviceId ID of the service to check
     */
    private void checkCredential(Long serviceId) {
        var service = serviceService.getServiceById(serviceId);
        checkCredential(service);
    }
}
