package com.lastminute.user.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class UserException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public UserException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public static Supplier<UserException> of(ExceptionCode exceptionCode) {
        return () -> new UserException(exceptionCode);
    }

}
