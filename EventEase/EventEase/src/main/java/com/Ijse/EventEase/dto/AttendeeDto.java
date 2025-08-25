package com.Ijse.EventEase.dto;

import com.Ijse.EventEase.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class AttendeeDto {
    private Long id;
    private String name;
    private String email;
    private Role role; // ORGANIZER / ATTENDEE
}
