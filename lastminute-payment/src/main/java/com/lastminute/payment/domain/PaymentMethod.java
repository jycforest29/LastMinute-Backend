package com.lastminute.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CARD("CARD"),
    PHONE("PHONE");

    private final String value;
}
