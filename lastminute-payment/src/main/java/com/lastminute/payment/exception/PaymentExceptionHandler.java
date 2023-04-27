package com.lastminute.payment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentExceptionHandler {
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Object> handleException(PaymentException exception, HttpServletRequest httpServletRequest){
        return ResponseEntity.status(exception.getExceptionCode().getHttpStatus())
                .body(exception.getExceptionCode().getMessage());
    }
}
