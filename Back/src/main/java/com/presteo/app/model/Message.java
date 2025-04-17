package com.presteo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1", referencedColumnName = "id")
    @NotNull
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2", referencedColumnName = "id")
    @NotNull
    private User user2;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "send_at", nullable = false)
    private String sendAt;

    @Column(name = "read_at", nullable = false)
    private Boolean isRead = false;
}
