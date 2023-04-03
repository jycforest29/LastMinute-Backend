package com.lastminute.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    @DisplayName("회원탈퇴 테스트")
    public void withDrawUser() {
        // given
        User user = User.builder()
                .nickname("james")
                .providerType(ProviderType.KAKAO)
                .build();

        // when
        user.withdraw();

        // then
        assertThat(user.accountState).isEqualTo(AccountState.WITHDRAWN);
        assertThat(user.getNickname()).isEqualTo("탈퇴한 사용자");
    }
}
