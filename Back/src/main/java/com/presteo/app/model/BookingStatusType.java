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

    public static BookingStatusType fromName(String displayName) {
        for (BookingStatusType statusType : BookingStatusType.values()) {
            if (statusType.getStatus().equalsIgnoreCase(displayName)) {
                return statusType;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + displayName);
    }
}
