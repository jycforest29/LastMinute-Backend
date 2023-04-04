package com.lastminute.user.common;

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
    @DisplayName("성공적으로 타입 변환")
    public void findSuccess() {
        // given
        TestDto dtoA = new TestDto("A");
        TestDto dtoB = new TestDto("B");

        // when
        Set<ConstraintViolation<TestDto>> violationsA = validator.validate(dtoA);
        Set<ConstraintViolation<TestDto>> violationsB = validator.validate(dtoB);

        // then
        assertThat(violationsA).isEmpty();
        assertThat(violationsB).isEmpty();
        assertThat(TestType.findByKey(dtoA.testType)).isEqualTo(TestType.TypeA);
        assertThat(TestType.findByKey(dtoB.testType)).isEqualTo(TestType.TypeB);
    }

    @Test
    @DisplayName("해당하는 타입이 없으면 ConstraintViolation이 발생한다.")
    public void constraintViolation() {
        // given
        TestDto dto = new TestDto("D");

        // when, then
        assertThatThrownBy(() -> TestType.findByKey(dto.testType))
                .isInstanceOf(EnumNotFoundException.class);
    }

    @Test
    @DisplayName("null이면 ConstraintViolation이 발생한다.")
    public void nonViolationInNull() {
        // given
        TestDto dto = new TestDto(null);

        // when
        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("findByKey에서 EnumNotFoundException이 발생한다.")
    public void enumNotFoundException() {
        // given
        TestDto dto = new TestDto("D");

        // when
        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
    }

    static class TestDto {

        @EnumValidator(enumClass = TestType.class, message = "enum 변환에 실패했습니다.")
        private final String testType;

        public TestDto(String testType) {
            this.testType = testType;
        }
    }

    enum TestType implements Convertable {

        TypeA("A"),
        TypeB("B");


        private final String key;

        TestType(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

        public static TestType findByKey(String key) {
            return Convertable.findByKey(key, TestType.class);
        }
    }
}
