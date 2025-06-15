package com.presteo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    @NotNull
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @NotNull
    private User customer;

    @Column(nullable = false)
    @NotNull
    private Integer rating;

    @Column(length = 1000)
    private String reviewText;

    @Column(nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;
} 