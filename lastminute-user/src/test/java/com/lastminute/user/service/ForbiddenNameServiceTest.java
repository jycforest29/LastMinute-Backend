package com.lastminute.user.service;

import com.lastminute.user.domain.ForbiddenName;
import com.lastminute.user.exception.UserException;
import com.lastminute.user.external.dto.CreateForbiddenNameRequestDto;
import com.lastminute.user.external.dto.ReadForbiddenNameResponseDto;
import com.lastminute.user.repository.ForbiddenNameRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ForbiddenNameServiceTest {

    @Mock
    private ForbiddenNameRepository forbiddenNameRepository;

    @InjectMocks
    private ForbiddenNameService forbiddenNameService;

    @Nested
    @DisplayName("금지된 이름 추가")
    class createForbiddenName {

        @Test
        @DisplayName("추가 성공")
        public void createSuccess() {
            // given
            final String name = "root";
            CreateForbiddenNameRequestDto request = CreateForbiddenNameRequestDto.builder()
                    .name(name)
                    .reason("confuse")
                    .build();

            given(forbiddenNameRepository.findById(name)).willReturn(Optional.empty());
            given(forbiddenNameRepository.save(any())).willReturn(request.toEntity());

            // when
            ReadForbiddenNameResponseDto response = forbiddenNameService.createForbiddenName(request);

            // then
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getReason()).isEqualTo(request.getReason());
        }

        @Test
        @DisplayName("이미 존재하는 금지어 예외 발생")
        public void duplicateFail() {
            // given
            final String name = "root";
            CreateForbiddenNameRequestDto request = CreateForbiddenNameRequestDto.builder()
                    .name(name)
                    .reason("confuse")
                    .build();

            given(forbiddenNameRepository.findById(name)).willReturn(Optional.of(request.toEntity()));

            // when, then
            assertThatThrownBy(() -> forbiddenNameService.createForbiddenName(request))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("이미 등록된");
        }
    }

    @Nested
    @DisplayName("금지된 이름 삭제")
    class deleteForbiddenName {

        @Test
        @DisplayName("삭제 성공")
        public void deleteSuccess() {
            // given
            final String name = "root";
            ForbiddenName forbiddenName = ForbiddenName.builder()
                    .name(name)
                    .reason("confuse")
                    .build();

            given(forbiddenNameRepository.findById(name)).willReturn(Optional.of(forbiddenName));
            doNothing().when(forbiddenNameRepository).delete(any());

            // when
            forbiddenNameService.deleteForbiddenName(name);

            // then
            then(forbiddenNameRepository).should(times(1)).delete(any());
        }

        @Test
        @DisplayName("해당 금지어가 없어서 실패")
        public void notFoundFail() {
            // given
            final String name = "root";
            given(forbiddenNameRepository.findById(anyString())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> forbiddenNameService.deleteForbiddenName(name))
                    .isInstanceOf(UserException.class);
        }
    }

    @Nested
    @DisplayName("금지된 이름 여부 확인")
    class isForbidden {

        @Test
        @DisplayName("대상 이름이 금지어 일 때")
        public void returnTrue() {
            // given
            final String name = "forbidden";
            ForbiddenName forbiddenName = ForbiddenName.builder()
                    .name(name)
                    .reason("confuse")
                    .build();

            given(forbiddenNameRepository.findById(name)).willReturn(Optional.of(forbiddenName));

            // when
            final boolean isForbidden = forbiddenNameService.isForbiddenName(name);

            // then
            assertThat(isForbidden).isEqualTo(true);
        }

        @Test
        @DisplayName("대상 이름이 금지어가 아닐 때")
        public void returnFalse() {
            // given
            final String name = "forbidden";
            given(forbiddenNameRepository.findById(name)).willReturn(Optional.empty());

            // when
            final boolean isForbidden = forbiddenNameService.isForbiddenName(name);

            // then
            assertThat(isForbidden).isEqualTo(false);
        }
    }

}
