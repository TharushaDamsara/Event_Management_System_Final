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
public class RegistrationAop {
    @Pointcut("execution(* com.Ijse.EventEase.controller.RegistrationController.registerUser(..))")
    public void registerUser() {
        log.info("Registering user");
    }

    @Before("registerUser()")
    public void logBeforeRegisterUser() {
        log.info("Registering user");
    }

    @After("registerUser()")
    public void logAfterRegisterUser() {
        log.info("User registered successfully");
    }
}
