package com.lastminute.payment.external.dto;

import com.lastminute.payment.domain.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReadPaymentResponseDto {
    private final String paymentMethod;
    private final Integer installmentPeriod;
    private final Integer originalPrice;
    private final Integer fee;
    private final Integer finalPrice;
    private final LocalDateTime acceptedAt;
    private final Boolean cancelAvailable;
    public static ReadPaymentResponseDto of(Payment entity){
        return ReadPaymentResponseDto.builder()
                .paymentMethod(entity.getPaymentMethod().getKey())
                .installmentPeriod(entity.getInstallmentPeriod().getKey())
                .originalPrice(entity.getOriginalPrice())
                .fee(entity.getFee())
                .finalPrice(entity.getFinalPrice())
                .acceptedAt(entity.getAcceptedAt())
                .cancelAvailable(entity.getCancelAvailable())
                .build();
    }
}
