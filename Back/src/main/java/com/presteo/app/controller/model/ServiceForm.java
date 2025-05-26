package com.presteo.app.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ServiceForm {
    @NotNull
    private Long providerId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String domain;

    @NotNull
    private String city;

    @NotNull
    private Double price;

    @NotNull
    private int durationHours;

    private MultipartFile imageFile;
}
