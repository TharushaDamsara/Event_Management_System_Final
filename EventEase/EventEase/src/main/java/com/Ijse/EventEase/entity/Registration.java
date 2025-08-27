package com.Ijse.EventEase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Attendee (User who registers)
    @ManyToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private User attendee;

    // Event they register for
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private LocalDateTime registrationTime;

    private String qrCodePath; // Store QR image path OR base64 string

    private boolean emailSent;

    private boolean whatsappSent;
}
