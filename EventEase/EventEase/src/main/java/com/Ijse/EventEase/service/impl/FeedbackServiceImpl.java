package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.*;
import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.entity.Feedback;
import com.Ijse.EventEase.entity.User;
import com.Ijse.EventEase.exception.DuplicateFeedbackException;
import com.Ijse.EventEase.exception.NotFoundException;
import com.Ijse.EventEase.repository.EventRepository;
import com.Ijse.EventEase.repository.FeedbackRepository;
import com.Ijse.EventEase.repository.UserRepository;
import com.Ijse.EventEase.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor

public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ApiResponce addFeedback(FeedbackDto feedbackDto) throws DuplicateFeedbackException, NotFoundException {
        if(feedbackRepository.findById(feedbackDto.getId()).isPresent()){
            throw new DuplicateFeedbackException("Feedback already exists");
        }
        User attendee = userRepository.findById(feedbackDto.getAttendee())
                .orElseThrow(() -> new NotFoundException("Attendee not found"));

        Event event = eventRepository.findById(feedbackDto.getEvent())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        Feedback feedback = mapToFeedback(feedbackDto, attendee, event);
        feedbackRepository.save(feedback);
        return new ApiResponce(200, "Feedback added successfully", true);
    }

    @Override
    public ApiResponce getAllFeedbackbyEventId(Long id) throws NotFoundException {
        List<Feedback> feedbacks = feedbackRepository.findAllByEvent_Id(id);

        if (feedbacks.isEmpty()) {
            throw new NotFoundException("No Feedback found for event ID: " + id);
        }

        // Convert entities -> DTOs
        List<FeedbackDto> feedbackDtos = feedbacks.stream()
                .map(this::mapToFeedbackDto) // create this helper method
                .toList();

        return new ApiResponce(200, "Feedbacks for this event", feedbackDtos);
    }

    // Mapper method
    private FeedbackDto mapToFeedbackDto(Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .feedback(feedback.getComment())
                .rating(feedback.getRating())
                .attendee(feedback.getAttendee().getId()) // safer than exposing whole User
                .event(feedback.getEvent().getId())
                .submittedAt(feedback.getSubmittedAt())
                .build();
    }



    @Override
    public ApiResponce getAllFeedbackbyAttendeeEmail(String email) throws NotFoundException {
        List<Feedback> feedbacks = feedbackRepository.findAllByAttendee_Email(email);
        if (feedbacks.isEmpty()){
            throw new NotFoundException("No Feedback from this attendee");
        }
        List<FeedbackDto> feedbackDtos = feedbacks.stream()
                .map(this::mapToFeedbackDto) // create this helper method
                .toList();
        return new ApiResponce(200, "Feedbacks from this attendee",feedbackDtos);
    }


    public Feedback mapToFeedback(FeedbackDto feedbackDto, User attendee, Event event) {
        return Feedback.builder()
                .comment(feedbackDto.getFeedback())
                .rating(feedbackDto.getRating())
                .attendee(attendee)   // attach the User entity
                .event(event)         // attach the Event entity
                .submittedAt(LocalDateTime.now())
                .build();
    }

}
