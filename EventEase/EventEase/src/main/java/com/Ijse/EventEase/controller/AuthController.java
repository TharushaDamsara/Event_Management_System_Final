package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.*;
import com.Ijse.EventEase.enums.Role;
import com.Ijse.EventEase.exception.EmailDuplicateException;
import com.Ijse.EventEase.exception.UserEmailNotFoundException;
import com.Ijse.EventEase.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class AuthController {

    private final UserService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponce> registerUser(@RequestBody RegisterDTO registerDTO){
        try {
            return ResponseEntity.ok(
                    new ApiResponce(200, "User registered successfully", authService.registerUser(registerDTO))
            );
        } catch (EmailDuplicateException e) {
            return ResponseEntity.status(400)
                    .body(new ApiResponce(400, e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponce> login(@RequestBody AuthDTO authDTO){
        try {
            // Ensure role is uppercase
            String role = String.valueOf(authDTO.getRole());
            authDTO.setRole(Role.valueOf(role));

            // Authenticate user (returns token or user info)
            AuthResponceDto authenticate = authService.authenticate(authDTO);

            return ResponseEntity.ok(new ApiResponce(200, "OK", authenticate));

        }catch (UserEmailNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(401) // unauthorized
                    .body(new ApiResponce(401, "Invalid credentials", false));
        }
    }
}
