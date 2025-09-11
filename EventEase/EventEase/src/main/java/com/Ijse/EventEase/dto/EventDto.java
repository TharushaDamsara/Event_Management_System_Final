package com.Ijse.EventEase.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDto {

    private Long id; // for updates

    @NotBlank(message = "Title is required")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 500)
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Event date is required")
    @FutureOrPresent
    private LocalDate eventDate;

    @NotNull(message = "Event time is required")
    private LocalTime eventTime;

    @Positive
    private int maxAttendees;

    private Long organizerId;

    @NotEmpty(message = "At least one ticket is required")
    private List<TicketDto> tickets; // list of tickets
}
