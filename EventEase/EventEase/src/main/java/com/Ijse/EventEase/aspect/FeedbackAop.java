package com.Ijse.EventEase.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class FeedbackAop {
    @Pointcut("execution(* com.Ijse.EventEase.controller.FeedbackController.addFeedback(..)")
    public void createFeedback() {
        log.info("Creating feedback");
    }

    @Before("addFeedback()")
    public void logBeforeCreateFeedback() {
        log.info("Creating feedback");
    }

    @After("addFeedback()")
    public void logAfterCreateFeedback() {
        log.info("Feedback created successfully");
    }

}
