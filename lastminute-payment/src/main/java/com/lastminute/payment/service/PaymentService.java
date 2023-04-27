package com.lastminute.payment.service;

import com.lastminute.payment.exception.ExceptionCode;
import com.lastminute.payment.exception.PaymentException;
import com.lastminute.payment.external.dto.CreatePaymentRequestDto;
import com.lastminute.payment.external.dto.CreatePaymentBridgeDto;
import com.lastminute.payment.external.dto.ReadPaymentResponseDto;
import com.lastminute.payment.domain.Payment;
import com.lastminute.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PgAgencyAdapter pgAgencyAdapter;

    @Transactional
    public ReadPaymentResponseDto createPayment(CreatePaymentRequestDto request){
        CreatePaymentBridgeDto bridge = pgAgencyAdapter.createPayment(request)
                .orElseThrow(() -> new PaymentException(ExceptionCode.PAYMENT_CREATION_FAILED));

        Payment payment = bridge.toEntity();
        paymentRepository.save(payment);
        return ReadPaymentResponseDto.of(payment);
    }

    @Transactional(readOnly = true)
    public ReadPaymentResponseDto readPayment(Long bidId){
        return null;
    }

    @Transactional(readOnly = true)
    public ReadPaymentResponseDto readAllPayment(Long userId){
        // TODO: USER가 판매자 혹은 구매자인 모든 BID 불러오기
        // TODO: 해당 BID들과 매핑된 모든 결제 내역 리턴
        return null;
    }

    @Transactional
    public void deletePayment(Long paymentId){
        Payment payment = findPaymentInternal(paymentId);
        paymentRepository.delete(payment);
    }

    private Payment findPaymentInternal(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(ExceptionCode.PAYMENT_NOT_FOUND));
    }
}
