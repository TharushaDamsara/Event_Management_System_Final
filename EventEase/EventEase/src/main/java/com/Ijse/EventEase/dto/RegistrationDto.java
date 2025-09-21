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
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String ticketType;
    private String paymentMethode;
    private double amount;
    private long attendeeId;
    private long eventId;

}
