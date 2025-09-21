package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.RegisterDTO;
import com.Ijse.EventEase.dto.RegistrationDto;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.exception.RegistrationNotFoundException;
import com.Ijse.EventEase.exception.UserNotFoundException;
import com.Ijse.EventEase.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/registration")
@CrossOrigin
@Slf4j

public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponce> registerUser(@RequestBody RegistrationDto registrationDto) {
        try {
            ApiResponce apiResponce = registrationService.registerRegistration(registrationDto);

            if (apiResponce.getCode() == 409) { // already registered
                return ResponseEntity.status(409).body(apiResponce);
            }
            return ResponseEntity.ok(apiResponce);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponce> updateUser(@PathVariable Long id, @RequestBody RegistrationDto registrationDto) {
        try {
            ApiResponce apiResponce = registrationService.updateRegistration(id, registrationDto);
            return ResponseEntity.status(200).body(apiResponce);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        } catch (RegistrationNotFoundException e) {
            throw new RuntimeException(e);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        }
    }
        @DeleteMapping("/delete/{id}")
                public ResponseEntity<ApiResponce> deleteUser(@PathVariable Long id) {
            try {
                ApiResponce apiResponce = registrationService.deleteRegistration(id);
                return ResponseEntity.status(200).body(apiResponce);

            } catch (RegistrationNotFoundException e) {
                return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
            }
        }
        @GetMapping("/byAttendeeId/{id}")
        public ResponseEntity<ApiResponce> getRegistrationByAtendeeId(@PathVariable Long id) {
        try {
            ApiResponce registrationByAttendeeId = registrationService.getRegistrationByAttendeeId(id);
            return ResponseEntity.status(200).body(registrationByAttendeeId);
        } catch (RegistrationNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        }
        }
        @GetMapping("/byEventId/{id}")
        public ResponseEntity<ApiResponce> getRegistrationByEventId(@PathVariable Long id) {
        try {

            ApiResponce registrationByEventId = registrationService.getRegistrationByEventId(id);
            return ResponseEntity.status(200).body(registrationByEventId);
        } catch (RegistrationNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        }
        }

    }

