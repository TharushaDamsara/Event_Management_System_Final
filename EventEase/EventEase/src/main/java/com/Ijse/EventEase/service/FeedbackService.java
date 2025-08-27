package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.ApiResponce;

import com.Ijse.EventEase.dto.FeedbackDto;
import com.Ijse.EventEase.exception.DuplicateFeedbackException;
import com.Ijse.EventEase.exception.NotFoundException;

public interface FeedbackService {
   ApiResponce addFeedback(FeedbackDto feedbackDto) throws DuplicateFeedbackException, NotFoundException;
   ApiResponce getAllFeedbackbyEventId(Long eventId) throws NotFoundException;
    ApiResponce getAllFeedbackbyAttendeeEmail(String email) throws NotFoundException;
}
