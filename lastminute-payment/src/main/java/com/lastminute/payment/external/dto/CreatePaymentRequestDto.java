package com.lastminute.payment.external.dto;

import com.lastminute.payment.domain.InstallmentPeriod;
import com.lastminute.payment.domain.PaymentMethod;
import com.lastminute.payment.common.EnumValidator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentRequestDto {

    @NotNull
    @EnumValidator(enumClass = PaymentMethod.class, message = "지원하지 않는 결제 방식 입니다.")
    private final String paymentMethod;

    @NotNull
    @EnumValidator(enumClass = InstallmentPeriod.class, message = "지원하지 않는 할부 기간 입니다.")
    private final Integer installmentPeriod;

    @NotNull
    @Min(0) @Max(1000000)
    private final Integer originalPrice;
}
