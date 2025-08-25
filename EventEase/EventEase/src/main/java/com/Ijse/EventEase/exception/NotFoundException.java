package com.Ijse.EventEase.exception;

public class NotFoundException extends Throwable {
    public NotFoundException(String feedbackNotFound) {
        super(feedbackNotFound);
    }
}
