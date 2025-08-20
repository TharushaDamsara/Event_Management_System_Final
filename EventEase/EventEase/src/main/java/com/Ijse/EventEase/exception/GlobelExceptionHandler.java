package com.Ijse.EventEase.exception;


import com.Ijse.EventEase.dto.ApiResponce;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobelExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ApiResponce handleRuntimeException(RuntimeException e) {
        return new ApiResponce(400, e.getMessage(), false);
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ApiResponce handleUserNameNotFoundException(Exception e) {
        return new ApiResponce(404, e.getMessage(), false);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ApiResponce handleEmailDuplicateException(Exception e) {
        return new ApiResponce(409, e.getMessage(), false);
    }
    @ExceptionHandler(DuplicateEventException.class)
    public ApiResponce handleDuplicateEventException(Exception e) {
        return new ApiResponce(409, e.getMessage(), false);
    }
    @ExceptionHandler(EventNotFoundException.class)
    public ApiResponce handleEventNotFoundException(Exception e) {
        return new ApiResponce(404, e.getMessage(), false);
    }

}
