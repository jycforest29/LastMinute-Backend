package com.lastminute.user.external.dto;

import com.lastminute.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class UserResponseDto {
    private final Long id;
    private final String nickname;
    private final String email;

    public static UserResponseDto of(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .build();
    }
}
