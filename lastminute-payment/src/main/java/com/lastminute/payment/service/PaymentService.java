package com.lastminute.payment.service;

import com.lastminute.external.dto.CreatePaymentRequestDto;
import com.lastminute.external.dto.CreatePaymentResponseDto;
import com.lastminute.external.dto.ReadPaymentResponseDto;
import com.lastminute.payment.domain.Payment;
import com.lastminute.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public CreatePaymentResponseDto createPayment(CreatePaymentRequestDto request, Long userId){
        // TODO: request를 PG사 API 호출하는 메서드의 인자로 사용
        // TODO: 해당 메서드로부터 리턴된 값을 Payment 엔티티로 매핑
        // TODO: save() 후 CreatePaymentResponseDto로 변환
        return null;
    }

    @Transactional(readOnly = true)
    public ReadPaymentResponseDto readPayment(Long paymentId){
        Payment payment = findPaymentInternal(paymentId);
        return ReadPaymentResponseDto.of(payment);
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
                .orElseThrow(() -> new RuntimeException("PAYMENT_NOT_FOUND")); // TODO: 커스텀 에러코드 생성하기
    }
}
