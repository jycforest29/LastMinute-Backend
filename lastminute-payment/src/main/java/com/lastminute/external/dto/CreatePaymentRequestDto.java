package com.lastminute.external.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentRequestDto { // TODO: validation 추가하
    private final String paymentMethod;
    private final Integer installmentPeriod;
    private final Integer originalPrice;
}
