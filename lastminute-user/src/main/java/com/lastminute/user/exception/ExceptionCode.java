package com.lastminute.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 없습니다."),
    USER_ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "이미 탈퇴한 사용자입니다."),
    USER_NAME_NOT_ALLOWED(HttpStatus.UNPROCESSABLE_ENTITY, "사용할 수 없는 닉네임입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
