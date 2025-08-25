package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.*;
import com.Ijse.EventEase.entity.Feedback;
import com.Ijse.EventEase.exception.DuplicateFeedbackException;
import com.Ijse.EventEase.exception.NotFoundException;
import com.Ijse.EventEase.repository.FeedbackRepository;
import com.Ijse.EventEase.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor

public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public ApiResponce addFeedback(FeedbackDto feedbackDto) throws DuplicateFeedbackException {
        if(feedbackRepository.findById(feedbackDto.getId()).isPresent()){
            throw new DuplicateFeedbackException("Feedback already exists");
        }
        Feedback feedback = mapToFeedback(feedbackDto);
        feedbackRepository.save(feedback);
        return new ApiResponce(200, "Feedback added successfully", true);
    }

    @Override
    public ApiResponce getAllFeedbackbyEventId(Long id) throws NotFoundException {
        List<Feedback> feedbacks = feedbackRepository.findAllByEvent_Id(id);
        if(feedbacks.isEmpty()){
            throw new NotFoundException("No Feedback for this event");
        }
        return new ApiResponce(200, "Feedbacks for this event",feedbacks);

    }


    @Override
    public ApiResponce getAllFeedbackbyAttendeeEmail(String email) throws NotFoundException {
        List<Feedback> feedbacks = feedbackRepository.findAllByAttendee_Email(email);
        if (feedbacks.isEmpty()){
            throw new NotFoundException("No Feedback from this attendee");
        }
        return new ApiResponce(200, "Feedbacks from this attendee",feedbacks);
    }


    public Feedback mapToFeedback(FeedbackDto feedbackDto){
        return Feedback.builder()
                // ❌ remove id() here
                .comment(feedbackDto.getFeedback())
                .rating(feedbackDto.getRating())
                .attendee(feedbackDto.getAttendee())
                .event(feedbackDto.getEvent())
                .submittedAt(java.time.LocalDateTime.now())
                .build();
    }

}
