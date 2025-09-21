package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
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

    @GetMapping("/eventbyemail/{email}")
    public ResponseEntity<ApiResponce> getEventByEmail(@PathVariable String email) {
        try {
            ApiResponce response = eventService.getEventBYEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/count/{email}")
    public ResponseEntity<ApiResponce> countEventsByOrganizerEmail(@PathVariable String email) {
        try {
            ApiResponce response = eventService.countEventsByOrganizerEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/bydateandemail")
    public ResponseEntity<ApiResponce> getEventsByDateAndEmail(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("email") String email) {
        try {
            ApiResponce response = eventService.getOrganizerEventBYDate(date, email);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }


    @GetMapping("/eventbydate")
    public ResponseEntity<ApiResponce> getAllEventByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            ApiResponce response = eventService.getAllEventBYDate(date);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException | Exception e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponce> getAllEvents() {
        try {
            ApiResponce response = eventService.getAllEvents();
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/eventbyid/{eventId}")
    public ResponseEntity<ApiResponce> getEventById(@PathVariable Long eventId) {
        try {
            ApiResponce response = eventService.findEventById(eventId);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/eventbyorganizerid/{userId}")
    public ResponseEntity<ApiResponce> getEventsByOrganizerId(@PathVariable Long userId) {
        try {
            ApiResponce response = eventService.findEventsByOrganizerId(userId);
            return ResponseEntity.ok(response);
        } catch (Exception | EventNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponce(404, e.getMessage(), false));
        }
    }
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<ApiResponce> getStatisticsByOrganizerId(@PathVariable Long userId) {
        try {
            ApiResponce response = eventService.getStatisticsByOrganizerId(userId);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponce(404, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponce(500, e.getMessage(), false));
        }
    }

}
