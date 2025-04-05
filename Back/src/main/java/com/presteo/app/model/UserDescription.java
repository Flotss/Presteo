package com.presteo.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_descriptions")
public class UserDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false, length = 1000)
    private String description;
}
