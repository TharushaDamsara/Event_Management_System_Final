package com.Ijse.EventEase.exception;

public class DuplicateFeedbackException extends Throwable {
    public DuplicateFeedbackException(String feedbackAlreadyExists) {
        super(feedbackAlreadyExists);
    }
}
