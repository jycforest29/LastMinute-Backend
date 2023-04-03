package com.lastminute.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 12)
    private String nickname;

    @Column(length = 255)
    private String email;

    @NotNull
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    AccountRole accountRole = AccountRole.USER;

    @NotNull
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    AccountState accountState = AccountState.NORMAL;

    @NotNull
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    ProviderType providerType;

    public void withdraw() {
        this.accountState = AccountState.WITHDRAWN;
        this.email = null;
        this.nickname = "탈퇴한 사용자";
    }

    @Builder
    public User(String nickname, String email, AccountRole accountRole, AccountState accountState, ProviderType providerType) {
        this.nickname = nickname;
        this.email = email;
        this.accountRole = accountRole;
        this.accountState = accountState;
        this.providerType = providerType;
    }
}
