package com.lastminute.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 결제 내역과 일치하는 정보가 없습니다"),
    PAYMENT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "내부적으로 발생한 에러로 결제에 실패했습니다.");
//    PAYMENT_METHOD_NOT_AUTHENTICATED(HttpStatus.BAD_REQUEST, "결제 수단 정보가 올바르지 않습니다"),
//    INSUFFICIENT_CASH(HttpStatus.PRECONDITION_REQUIRED, "잔액이 부족합니다");

    private final HttpStatus httpStatus;
    private final String message;
}
