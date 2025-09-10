package com.Ijse.EventEase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class RegistrationDto {
    private Long id;
    private long attendeeId;
    private long eventId;
    private LocalDateTime registrationTime;
    private String qrCodePath;
    private boolean emailSent;
    private boolean whatsappSent;
}
