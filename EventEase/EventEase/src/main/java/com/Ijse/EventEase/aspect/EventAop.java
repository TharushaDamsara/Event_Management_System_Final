package com.Ijse.EventEase.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect

public class EventAop {
    @Pointcut("execution(* com.Ijse.EventEase.controller.EventController.createEvent(..))")
    public void createEvent() {
        log.info("Creating event");
    }

    @Before("createEvent()")
    public void logBeforeCreateEvent() {
        log.info("Creating event");
    }

    @After("createEvent()")
    public void logAfterCreateEvent() {
        log.info("Event created successfully");
    }

    @Pointcut("execution(* com.Ijse.EventEase.controller.EventController.updateEvent(..))")
    public void updateEvent() {
        log.info("Updating event");
    }

    @Before("updateEvent()")
    public void logBeforeUpdateEvent() {
        log.info("Updating event");
    }

    @After("updateEvent()")
    public void logAfterUpdateEvent() {
        log.info("Event updated successfully");
    }

}
