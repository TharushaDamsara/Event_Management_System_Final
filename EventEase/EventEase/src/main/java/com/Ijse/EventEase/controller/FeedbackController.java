package com.Ijse.EventEase.controller;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.FeedbackDto;
import com.Ijse.EventEase.exception.DuplicateFeedbackException;
import com.Ijse.EventEase.exception.NotFoundException;
import com.Ijse.EventEase.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<ApiResponce> addFeedback(@Valid @RequestBody FeedbackDto feedbackDto){
        try {
            ApiResponce apiResponce = feedbackService.addFeedback(feedbackDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponce);
        } catch (DuplicateFeedbackException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponce(409, e.getMessage(), false));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponce(404, e.getMessage(), false));
        }
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponce> getAllFeedbackbyEvent(@PathVariable Long eventId){

        try {
            ApiResponce apiResponce = feedbackService.getAllFeedbackbyEventId(eventId);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponce);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponce(404, e.getMessage(), false));
        }

    }

    @GetMapping("/attendee/{email}")
    public ResponseEntity<ApiResponce> getAllFeedbackbyAttendee(@PathVariable String email){
        try {
            ApiResponce apiResponce = feedbackService.getAllFeedbackbyAttendeeEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponce);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponce(404, e.getMessage(), false));
        }
    }
}
