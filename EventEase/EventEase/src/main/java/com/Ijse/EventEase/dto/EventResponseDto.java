package com.Ijse.EventEase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String bannerImageUrl;
    private int maxAttendees;

    private OrganizerDto organizer; // nested DTO
}
