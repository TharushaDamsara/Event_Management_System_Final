package com.Ijse.EventEase.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"attendee", "event"}) // 👈 prevents recursion
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String ticketType;
    private String paymentMethode;
    private double amount;

    @ManyToOne
    @JsonIgnoreProperties("registrations")
    @JoinColumn(name = "attendee_id", nullable = false)
    private User attendee;

    @ManyToOne
    @JsonIgnoreProperties("registrations")
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private LocalDateTime registrationTime;
    private String qrCodePath; // Store QR image path OR base64 string
    private boolean emailSent;
    private boolean whatsappSent;
}
