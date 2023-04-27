package com.lastminute.payment.service;

import com.lastminute.payment.domain.Payment;
import com.lastminute.payment.exception.PaymentException;
import com.lastminute.payment.external.dto.CreatePaymentBridgeDto;
import com.lastminute.payment.external.dto.CreatePaymentRequestDto;
import com.lastminute.payment.external.dto.ReadPaymentResponseDto;
import com.lastminute.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PgAgencyAdapter pgAgencyAdapter;

    @InjectMocks
    private PaymentService paymentService;

    @Nested
    @DisplayName("결제 생성")
    class CreatePayment{

        String paymentMethod = "CARD";
        Integer installmentPeriod = 1;
        Integer originalPrice = 1000;

        CreatePaymentRequestDto request = CreatePaymentRequestDto.builder()
                .paymentMethod(paymentMethod)
                .installmentPeriod(installmentPeriod)
                .originalPrice(originalPrice)
                .build();

        @Test
        @DisplayName("Pg사로부터 결제 성공 응답이 왔을 때 성공")
        public void successCreatePayment(){
            // given
            CreatePaymentBridgeDto bridge = CreatePaymentBridgeDto.builder()
                    .pgId(1L)
                    .paymentMethod(paymentMethod)
                    .installmentPeriod(installmentPeriod)
                    .originalPrice(originalPrice)
                    .fee(1000)
                    .finalPrice(2000)
                    .build();

            given(pgAgencyAdapter.createPayment(any()))
                    .willReturn(Optional.ofNullable(bridge));

            // when
            ReadPaymentResponseDto readPaymentResponseDto = paymentService.createPayment(request);

            // then
            assertThat(readPaymentResponseDto.getPaymentMethod()).isEqualTo(paymentMethod);
            assertThat(readPaymentResponseDto.getInstallmentPeriod()).isEqualTo(installmentPeriod);
        }

        @Test
        @DisplayName("Pg사로부터 결제 실패 오류코드가 왔을 때 매칭되는 에러 메시지 전달")
        public void failCreatePayment(){
            // given
            given(pgAgencyAdapter.createPayment(any()))
                    .willReturn(Optional.ofNullable(null));

            // when, then
            assertThatThrownBy(() -> paymentService.createPayment(request))
                    .isInstanceOf(PaymentException.class);
        }
    }

    @Nested
    @DisplayName("결제 조회")
    class ReadPayment{

        @Test
        @DisplayName("이용 중인 사용자가 결제한 특정 결제 내역 리턴")
        public void readPayment(){

        }

        @Test
        @DisplayName("이용 중인 사용자가 결제한 모든 결제 내역 리턴")
        public void readPaymentAll(){

        }
    }

    @Nested
    @DisplayName("결제 취소")
    class DeletePayment{

        @Test
        @DisplayName("이용 중인 사용자가 결제한 결제가 취소 가능할 때 취소 성공")
        public void successDeletePayment(){

        }

        @Test
        @DisplayName("이용 중인 사용자가 결제한 결제가 취소 불가능할 때 취소 실패")
        public void failDeletePayment(){

        }
    }
}