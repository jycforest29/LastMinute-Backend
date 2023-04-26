package com.lastminute.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstallmentPeriod {
    ZERO(0),
    A_MONTH(1),
    QUARTER(3),
    HALF_YEAR(6),
    A_YEAR(12),
    TWO_YEAR(24);
    private final int period;
}
