package com.presteo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @NotNull
    private User provider;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false, length = 1000)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private String domain;

    @Column()
    private String imageUrl;

    @Column(nullable = false)
    @NotNull
    private Double price;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;
}
