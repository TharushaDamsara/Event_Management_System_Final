package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.AuthDTO;
import com.Ijse.EventEase.dto.RegisterDTO;
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
        return ResponseEntity.ok(
                new ApiResponce(
                        200,
                        "User registered successfully",
                        authService.registerUser(registerDTO)
                )
        );
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponce> login(@RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(new ApiResponce(200,
                "OK",authService.authenticate(authDTO)));
    }
}
