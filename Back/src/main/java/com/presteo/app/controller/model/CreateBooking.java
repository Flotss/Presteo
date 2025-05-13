package com.presteo.app.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CreateBooking {
    @NotNull(message = "Service ID cannot be null")
    public final Long serviceId;

    @NotNull(message = "Customer ID cannot be null")
    public final Long customerId;

    @NotNull(message = "Booking date cannot be null")
    public final Date bookingDate;

    @NotNull(message = "Additional info cannot be null")
    public final String additionalInfo;

    @NotNull(message = "Address cannot be null")
    public final String address;
}
