package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponce> createEvent(@Valid @RequestBody EventDto eventDto) {
        try {
            ApiResponce response = eventService.createEvent(eventDto);
            return ResponseEntity.status(201).body(response);
        } catch (Exception | DuplicateEventException e) {
            return ResponseEntity.status(400)
                    .body(new ApiResponce(400, e.getMessage(), false));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponce> updateEvent(@Valid @RequestBody EventDto eventDto) {
        try {
            ApiResponce response = eventService.updateEvent(eventDto);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(400)
                    .body(new ApiResponce(400, e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<ApiResponce> deleteEvent(@PathVariable Long eventId) {
        try {
            ApiResponce response = eventService.deleteEvent(eventId);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponce> getEventByEmail(@PathVariable String email) {
        try {
            ApiResponce response = eventService.getEventBYEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }
}
