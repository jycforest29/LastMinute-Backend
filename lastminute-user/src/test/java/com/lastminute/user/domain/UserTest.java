package com.lastminute.user.domain;

import com.lastminute.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    @DisplayName("회원탈퇴 테스트")
    public void withDrawUser() {
        // given
        User user = User.builder()
                .nickname("james")
                .providerType(ProviderType.KAKAO)
                .accountState(AccountState.NORMAL)
                .build();

        // when
        user.withdraw();

        // then
        assertThat(user.accountState).isEqualTo(AccountState.WITHDRAWN);
        assertThat(user.getNickname()).isEqualTo("탈퇴한 사용자");
    }

    @Test
    @DisplayName("탈퇴한 사용자 정보 수정 불가")
    public void withDrawnUserUpdate() {
        // given
        User user = User.builder()
                .nickname("james")
                .providerType(ProviderType.KAKAO)
                .accountState(AccountState.WITHDRAWN)
                .build();

        // when, then
        assertThatThrownBy(() -> user.updateProfile("nick", "test@gmail.com"))
                .isInstanceOf(UserException.class);
    }
}
