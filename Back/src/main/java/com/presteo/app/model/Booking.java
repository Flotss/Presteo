package com.presteo.app.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private User customer;

    @Column(nullable = false)
    private String additionalInfo;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date bookingDate;

    @Column(nullable = false)
    private Double finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatusType status;

    @Column(nullable = false)
    @CreationTimestamp
    private String createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private String updatedAt;
}
