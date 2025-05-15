package com.presteo.app.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdate {
    @NotNull(message = "Status of the booking cannot be null")
    private String status;
}