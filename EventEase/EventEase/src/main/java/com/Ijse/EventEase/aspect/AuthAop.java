package com.Ijse.EventEase.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class AuthAop {
    @Pointcut("execution(* com.Ijse.EventEase.controller.AuthController.registerUser(..))")
    public void registerUser() {

    }

    @Before("execution(* com.Ijse.EventEase.controller.AuthController.registerUser(..))")
    public void logBeforeRegisterUser() {
        log.info("Registering user");
    }

    @After("execution(* com.Ijse.EventEase.controller.AuthController.registerUser(..))")
    public void logAfterRegisterUser() {
        log.info("User registered successfully");
    }

    @Pointcut("execution(* com.Ijse.EventEase.controller.AuthController.login(..))")
    public void login() {
        log.info("Logging in");
    }

    @Before("login()")
    public void logBeforeLogin() {
        log.info("Logging in");
    }

    @After("login()")
    public void logAfterLogin() {
        log.info("User logged in successfully");
    }

}
