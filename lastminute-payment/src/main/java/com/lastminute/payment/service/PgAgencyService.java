package com.lastminute.payment.service;

import com.lastminute.payment.external.dto.CreatePaymentBridgeDto;
import com.lastminute.payment.external.dto.CreatePaymentRequestDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PgAgencyService {
    public Optional<CreatePaymentBridgeDto> createPayment(CreatePaymentRequestDto request) {
        // TODO: Pg사 연동 방식 정한 후 다시 작성
        return Optional.ofNullable(null);
    }

    public void deletePayment(Long pgId) {
    }
}
