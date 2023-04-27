package com.lastminute.payment.service;

import com.lastminute.payment.external.dto.CreatePaymentRequestDto;
import com.lastminute.payment.external.dto.CreatePaymentBridgeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PgAgencyAdapter {

    private final PgAgencyService pgAgencyService;

    public Optional<CreatePaymentBridgeDto> createPayment(CreatePaymentRequestDto request) {
        return pgAgencyService.createPayment(request);
    }

    public void deletePayment(Long pgId){
        pgAgencyService.deletePayment(pgId);
    }
}
