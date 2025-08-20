package com.Ijse.EventEase.exception;

public class EventNotFoundException extends Throwable {
    public EventNotFoundException(String eventNotFound) {
        super(eventNotFound);
    }
}
