package com.lastminute.user.exception;

public class EnumNotFoundException extends InternalException {

    public static final String DEFAULT_MESSAGE = "해당하는 enum이 없습니다.";

    public EnumNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EnumNotFoundException(String message) {
        super(message);
    }
}
