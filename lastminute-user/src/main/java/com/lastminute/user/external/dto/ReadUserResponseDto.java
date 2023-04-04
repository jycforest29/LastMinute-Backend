package com.lastminute.user.external.dto;

import com.lastminute.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class ReadUserResponseDto {
    private final Long id;
    private final String nickname;
    private final String email;

    public static ReadUserResponseDto of(User entity) {
        return ReadUserResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .build();
    }
}
