package com.Ijse.EventEase.dto;

import com.Ijse.EventEase.enums.Role;
import lombok.Data;


@Data
public class AuthDTO {
    private String email;
    private String password;
    private Role role;

}
