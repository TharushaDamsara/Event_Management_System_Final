package com.Ijse.EventEase.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDto {

    private Long id; // Usually auto-generated in DB, no need to validate

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be today or in the future")
    private LocalDate eventDate;

    @NotNull(message = "Event time is required")
    private LocalTime eventTime;

    @NotBlank(message = "Banner image URL is required")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Banner image URL must be a valid URL"
    )
    private String bannerImageUrl;

    @Positive(message = "Max attendees must be greater than 0")
    private int maxAttendees;

    private Long organizerId;
}
