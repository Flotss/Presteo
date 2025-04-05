package com.presteo.app.model;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    PROVIDER("PROVIDER");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    public static RoleType fromName(String displayName) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getName().equalsIgnoreCase(displayName)) {
                return roleType;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + displayName);
    }
}
