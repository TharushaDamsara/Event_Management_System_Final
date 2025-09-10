package com.Ijse.EventEase.dto;

import com.Ijse.EventEase.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class AuthResponceDto {
    private String token;
    private Role role;
}
