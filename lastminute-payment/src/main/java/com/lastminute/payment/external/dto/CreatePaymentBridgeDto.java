package com.lastminute.payment.external.dto;

import com.lastminute.payment.domain.InstallmentPeriod;
import com.lastminute.payment.domain.Payment;
import com.lastminute.payment.domain.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePaymentBridgeDto {
    private final Long pgId;
    private final String paymentMethod;
    private final Integer installmentPeriod;
    private final Integer originalPrice;
    private final Integer fee;
    private final Integer finalPrice;

    public Payment toEntity(){
        return Payment.builder()
                .pgId(this.pgId)
                .paymentMethod(PaymentMethod.findByKey(this.paymentMethod))
                .installmentPeriod(InstallmentPeriod.findByKey(this.installmentPeriod))
                .originalPrice(this.originalPrice)
                .fee(this.fee)
                .finalPrice(this.finalPrice)
                .build();
    }
}
