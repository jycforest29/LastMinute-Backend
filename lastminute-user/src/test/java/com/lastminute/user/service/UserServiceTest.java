package com.lastminute.user.service;

import com.lastminute.user.domain.*;
import com.lastminute.user.exception.UserException;
import com.lastminute.user.external.dto.UserRequestDto;
import com.lastminute.user.external.dto.UserResponseDto;
import com.lastminute.user.repository.ForbiddenNameRepository;
import com.lastminute.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ForbiddenNameRepository forbiddenNameRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("사용자 생성")
    class createUser {

        @BeforeEach
        public void setupEmptyForbiddenNames() {
            given(forbiddenNameRepository.findById(anyString())).willReturn(Optional.empty());
        }

        @Test
        @DisplayName("모든 값이 있고 정상적일 때 성공")
        public void createByFullInfo() {
            // given
            UserRequestDto request = UserRequestDto.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType("KAKAO")
                    .build();
            User createdUser = request.toEntity();
            Long userId = 412L;
            ReflectionTestUtils.setField(createdUser, "id", userId);

            given(userRepository.save(any())).willReturn(createdUser);

            // when
            UserResponseDto response = userService.createUser(request);

            // then
            assertThat(response.getId()).isEqualTo(userId);
            assertThat(response.getEmail()).isEqualTo(request.getEmail());
            assertThat(response.getNickname()).isEqualTo(request.getNickname());
        }

        @Test
        @DisplayName("필수 값만 있고 정상적일 때 성공")
        public void createByRequiredInfo() {
            // given
            UserRequestDto request = UserRequestDto.builder()
                    .nickname("james")
                    .providerType("KAKAO")
                    .build();
            User createdUser = request.toEntity();
            Long userId = 412L;
            ReflectionTestUtils.setField(createdUser, "id", userId);

            given(userRepository.save(any())).willReturn(createdUser);

            // when
            UserResponseDto response = userService.createUser(request);

            // then
            assertThat(response.getId()).isEqualTo(userId);
            assertThat(response.getEmail()).isNull();
            assertThat(response.getNickname()).isEqualTo(request.getNickname());
        }
        
        @Test
        @DisplayName("금지된 이름으로 생성 시 예외")
        public void notAllowedUserName() {
            // given
            final String name = "탈퇴한 사용자";
            UserRequestDto request = UserRequestDto.builder()
                    .nickname(name)
                    .providerType("KAKAO")
                    .build();
            User createdUser = request.toEntity();
            Long userId = 412L;
            ReflectionTestUtils.setField(createdUser, "id", userId);

            ForbiddenName forbiddenName = ForbiddenName.builder()
                    .name(name)
                    .reason("혼돈을 줄 수 있음")
                    .build();
            
            given(forbiddenNameRepository.findById(name)).willReturn(Optional.of(forbiddenName));

            // when, then
            assertThatThrownBy(() -> userService.createUser(request))
                    .isInstanceOf(UserException.class);
        }

    }

    @Nested
    @DisplayName("사용자 조회")
    class getUser {

        @Test
        @DisplayName("이용 중인 사용자 조회 성공")
        public void successGet() {
            // given
            User user = User.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType(ProviderType.KAKAO)
                    .accountRole(AccountRole.USER)
                    .accountState(AccountState.NORMAL)
                    .build();
            Long userId = 5326L;
            ReflectionTestUtils.setField(user, "id", userId);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            UserResponseDto response = userService.findUser(userId);

            // then
            assertThat(response.getId()).isEqualTo(userId);
            assertThat(response.getEmail()).isEqualTo(user.getEmail());
            assertThat(response.getNickname()).isEqualTo(user.getNickname());
        }

        @Test
        @DisplayName("해당하는 ID 없는 경우 예외")
        public void notFoundUser() {
            // given
            given(userRepository.findById(any())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findUser(14L))
                    .isInstanceOf(UserException.class);
        }

        @Test
        @DisplayName("닉네임이 '탈퇴한 사용자'로 조회")
        public void withdrawnUser() {
            // given
            User user = User.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType(ProviderType.KAKAO)
                    .accountRole(AccountRole.USER)
                    .accountState(AccountState.NORMAL)
                    .build();
            Long userId = 5326L;
            ReflectionTestUtils.setField(user, "id", userId);

            user.withdraw();
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            UserResponseDto response = userService.findUser(userId);

            // then
            assertThat(response.getId()).isEqualTo(userId);
            assertThat(response.getNickname()).isEqualTo("탈퇴한 사용자");
            assertThat(response.getEmail()).isNull();
        }
    }

    @Nested
    @DisplayName("사용자 탈퇴")
    class withdrawUser {

        @Test
        @DisplayName("탈퇴 성공 후 이메일 삭제, 닉네임 '탈퇴한 사용자', 상태 변경 적용")
        public void successWithdraw() {
            // given
            User user = User.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType(ProviderType.KAKAO)
                    .accountRole(AccountRole.USER)
                    .accountState(AccountState.NORMAL)
                    .build();
            Long userId = 5326L;
            ReflectionTestUtils.setField(user, "id", userId);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            userService.withdrawUser(userId);

            // then
            assertThat(user.getAccountState()).isEqualTo(AccountState.WITHDRAWN);
            assertThat(user.getNickname()).isEqualTo("탈퇴한 사용자");
            assertThat(user.getEmail()).isNull();
        }

        @Test
        @DisplayName("이미 탈퇴한 사용자 예외")
        public void alreadyWithdrawn() {
            // given
            User user = User.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType(ProviderType.KAKAO)
                    .accountRole(AccountRole.USER)
                    .accountState(AccountState.NORMAL)
                    .build();
            Long userId = 5326L;
            ReflectionTestUtils.setField(user, "id", userId);

            user.withdraw();
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.withdrawUser(userId))
                    .isInstanceOf(UserException.class);
        }
    }
}