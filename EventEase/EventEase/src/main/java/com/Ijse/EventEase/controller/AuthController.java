package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.AuthDTO;
import com.Ijse.EventEase.dto.RegisterDTO;
import com.Ijse.EventEase.exception.EmailDuplicateException;
import com.Ijse.EventEase.exception.UserEmailNotFoundException;
import com.Ijse.EventEase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class AuthController {

 private final UserService authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponce> registerUser(
            @RequestBody RegisterDTO registerDTO){
        try {
            return ResponseEntity.ok(
                    new ApiResponce(
                            200,
                            "User registered successfully",
                            authService.registerUser(registerDTO)
                    )
            );
        } catch (EmailDuplicateException e) {
            return ResponseEntity.ok(
                    new ApiResponce(
                            400,
                            e.getMessage(),
                            false
                    )
            );
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponce> login(@RequestBody AuthDTO authDTO){
        try {
            return ResponseEntity.ok(new ApiResponce(200,
                    "OK",authService.authenticate(authDTO)));
        } catch (UserEmailNotFoundException e) {
            return ResponseEntity.ok(
                    new ApiResponce(
                            404,
                            e.getMessage(),
                            false
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ApiResponce(
                            400,
                            "Invalid credentials",
                            false
                    )
            );
        }
    }
}
