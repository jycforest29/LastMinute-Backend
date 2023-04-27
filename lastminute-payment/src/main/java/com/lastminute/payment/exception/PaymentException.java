package com.lastminute.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
