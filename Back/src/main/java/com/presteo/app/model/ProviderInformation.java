package com.presteo.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "provider_information")
public class ProviderInformation {
    @Id
    private Long providerId;

    @Column(nullable = false)
    private String experience;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    @MapsId
    private User user;
}
