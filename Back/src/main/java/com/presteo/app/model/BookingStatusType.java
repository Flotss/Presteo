package com.presteo.app.model;

import lombok.Getter;

@Getter
public enum BookingStatusType {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    IN_PROGRESS("IN_PROGRESS"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED");

    private final String status;

    BookingStatusType(String status) {
        this.status = status;
    }
}
