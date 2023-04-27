package com.lastminute.payment.common;

import com.lastminute.payment.domain.InstallmentPeriod;
import com.lastminute.payment.domain.PaymentMethod;
import com.lastminute.payment.external.dto.CreatePaymentRequestDto;
import com.lastminute.user.exception.EnumNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EnumValidator 어노테이션")
public class EnumValidatorTest {
    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("DTO 검증에 성공한다")
    public void dtoValidationSuccess(){
        // given
        CreatePaymentRequestDto dto = CreatePaymentRequestDto.builder()
                .paymentMethod("CARD")
                .installmentPeriod(1)
                .originalPrice(1000)
                .build();

        // when
        Set<ConstraintViolation<CreatePaymentRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
        assertThat(PaymentMethod.findByKey(dto.getPaymentMethod())).isEqualTo(PaymentMethod.CARD);
        assertThat(InstallmentPeriod.findByKey(dto.getInstallmentPeriod())).isEqualTo(InstallmentPeriod.A_MONTH);
    }

    @Test
    @DisplayName("해당하는 결제 방식이 없으면 ConstraintViolation이 발생한다")
    public void constraintViolationForPaymentMethod(){
        // given
        CreatePaymentRequestDto dto = CreatePaymentRequestDto.builder()
                .paymentMethod("CASH")
                .installmentPeriod(1)
                .originalPrice(1000)
                .build();

        // when
        Set<ConstraintViolation<CreatePaymentRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
        assertThatThrownBy(() -> PaymentMethod.findByKey(dto.getPaymentMethod()))
                .isInstanceOf(EnumNotFoundException.class);
        assertThat(InstallmentPeriod.findByKey(dto.getInstallmentPeriod())).isEqualTo(InstallmentPeriod.A_MONTH);
    }

    @Test
    @DisplayName("해당하는 할부 기간이 없으면 ConstraintViolation이 발생한다")
    public void constraintViolationForInstallmentPeriod(){
        // given
        CreatePaymentRequestDto dto = CreatePaymentRequestDto.builder()
                .paymentMethod("CARD")
                .installmentPeriod(2)
                .originalPrice(1000)
                .build();

        // when
        Set<ConstraintViolation<CreatePaymentRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(PaymentMethod.findByKey(dto.getPaymentMethod())).isEqualTo(PaymentMethod.CARD);
        assertThatThrownBy(() -> InstallmentPeriod.findByKey(dto.getInstallmentPeriod()))
                .isInstanceOf(EnumNotFoundException.class);
    }

}
