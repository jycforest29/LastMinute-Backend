package com.lastminute.user.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public UserException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
