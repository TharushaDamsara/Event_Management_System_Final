package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/event")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventController {

    private final EventService eventService;

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponce> getEventByEmail(@PathVariable String email) {
        try {
            ApiResponce response = eventService.getEventBYEmail(email);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponce> createEvent(@RequestBody EventDto eventDto) {
        try {
            ApiResponce response = eventService.createEvent(eventDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
        } catch (DuplicateEventException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                    .body(new ApiResponce(409, e.getMessage(), false));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponce> updateEvent(@RequestBody EventDto eventDto) {
        try {
            ApiResponce response = eventService.updateEvent(eventDto);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<ApiResponce> deleteEvent(@PathVariable Long eventId) {
        try {
            ApiResponce response = eventService.deleteEvent(eventId);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }
}
