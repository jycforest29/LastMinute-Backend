package com.lastminute.user.service;

import com.lastminute.user.domain.*;
import com.lastminute.user.exception.UserException;
import com.lastminute.user.external.dto.CreateUserRequestDto;
import com.lastminute.user.external.dto.ReadUserResponseDto;
import com.lastminute.user.external.dto.UpdateUserRequestDto;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ForbiddenNameService forbiddenNameService;

    @Mock
    private UserWriteFacade userWriteFacade;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("사용자 생성")
    class createUser {

        @BeforeEach
        public void setupEmptyForbiddenNames() {
            given(forbiddenNameService.isForbiddenName(anyString())).willReturn(false);
        }

        @Test
        @DisplayName("모든 값이 있고 정상적일 때 성공")
        public void createByFullInfo() {
            // given
            CreateUserRequestDto request = CreateUserRequestDto.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType("KAKAO")
                    .build();
            User createdUser = request.toEntity();
            Long userId = 412L;
            ReflectionTestUtils.setField(createdUser, "id", userId);

            given(userWriteFacade.createUser(any())).willReturn(createdUser);

            // when
            ReadUserResponseDto response = userService.createUser(request);

            // then
            assertThat(response.getId()).isEqualTo(userId);
            assertThat(response.getEmail()).isEqualTo(request.getEmail());
            assertThat(response.getNickname()).isEqualTo(request.getNickname());
        }

        @Test
        @DisplayName("필수 값만 있고 정상적일 때 성공")
        public void createByRequiredInfo() {
            // given
            CreateUserRequestDto request = CreateUserRequestDto.builder()
                    .nickname("james")
                    .providerType("KAKAO")
                    .build();
            User createdUser = request.toEntity();
            Long userId = 412L;
            ReflectionTestUtils.setField(createdUser, "id", userId);

            given(userWriteFacade.createUser(any())).willReturn(createdUser);

            // when
            ReadUserResponseDto response = userService.createUser(request);

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
            CreateUserRequestDto request = CreateUserRequestDto.builder()
                    .nickname(name)
                    .providerType("KAKAO")
                    .build();
            User createdUser = request.toEntity();
            Long userId = 412L;
            ReflectionTestUtils.setField(createdUser, "id", userId);
            
            given(forbiddenNameService.isForbiddenName(name)).willReturn(true);

            // when, then
            assertThatThrownBy(() -> userService.createUser(request))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("사용할 수 없는 닉네임");
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
            ReadUserResponseDto response = userService.findUser(userId);

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
            ReadUserResponseDto response = userService.findUser(userId);

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
        @DisplayName("사용자 존재하면 정상적으로 탈퇴")
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
            given(userWriteFacade.withdrawUser(any())).willReturn(user);

            // when
            userService.withdrawUser(userId);

            // then
            then(userWriteFacade).should(times(1)).withdrawUser(user);
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

    @Nested
    @DisplayName("사용자 정보 수정")
    class updateUser {

        @Test
        @DisplayName("정상적으로 수정 완료")
        public void updateSuccess() {
            // given
            User user = User.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType(ProviderType.KAKAO)
                    .accountRole(AccountRole.USER)
                    .accountState(AccountState.NORMAL)
                    .build();
            Long userId = 1512L;
            ReflectionTestUtils.setField(user, "id", userId);

            given(forbiddenNameService.isForbiddenName(anyString())).willReturn(false);
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userWriteFacade.updateProfile(any())).willReturn(user);

            // when
            UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                    .userId(userId)
                    .email("different@gmail.com")
                    .nickname("nick")
                    .build();

            userService.updateUser(request);

            // then
            then(userWriteFacade).should(times(1)).updateProfile(user);
        }

        @Test
        @DisplayName("해당 ID의 사용자가 없을 때 예외")
        public void notFoundUser() {
            // given
            Long userId = 1512L;

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when, then
            UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                    .userId(userId)
                    .email("different@gmail.com")
                    .nickname("nick")
                    .build();

            assertThatThrownBy(() -> userService.updateUser(request))
                    .isInstanceOf(UserException.class);
        }

        @Test
        @DisplayName("금지된 이름으로 변경 시도 시 예외")
        public void forbiddenName() {
            // given
            User user = User.builder()
                    .email("myemail@gmail.com")
                    .nickname("james")
                    .providerType(ProviderType.KAKAO)
                    .accountRole(AccountRole.USER)
                    .accountState(AccountState.NORMAL)
                    .build();
            Long userId = 1512L;
            ReflectionTestUtils.setField(user, "id", userId);

            given(forbiddenNameService.isForbiddenName("root")).willReturn(true);
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when, then
            UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                    .userId(userId)
                    .email("different@gmail.com")
                    .nickname("root")
                    .build();

            assertThatThrownBy(() -> userService.updateUser(request))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("닉네임");
        }

    }
}
