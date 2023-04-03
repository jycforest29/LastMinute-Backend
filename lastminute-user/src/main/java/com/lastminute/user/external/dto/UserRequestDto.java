package com.lastminute.user.external.dto;

import com.lastminute.user.common.EnumValidator;
import com.lastminute.user.domain.ProviderType;
import com.lastminute.user.domain.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
public final class UserRequestDto {

    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 12, message = "2 ~ 12자의 이름을 입력해주세요.")
    private final String nickname;

    @Nullable
    @Size(max = 255, message = "255자를 초과한 이메일 주소는 입력할 수 없습니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull
    @EnumValidator(enumClass = ProviderType.class, message = "지원하지 않는 provider 입니다.")
    private final String providerType;

    public User toEntity() {
        return User.builder()
                .nickname(this.nickname)
                .email(this.email)
                .providerType(ProviderType.valueOf(providerType))
                .build();
    }
}
