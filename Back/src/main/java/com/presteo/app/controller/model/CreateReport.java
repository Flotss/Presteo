package com.presteo.app.controller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateReport {
    @NotNull
    private Long userReportedId;

    @NotNull
    private Long userReporterId;

    @NotBlank
    private String description;
}
