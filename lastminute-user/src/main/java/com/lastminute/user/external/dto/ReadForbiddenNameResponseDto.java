package com.lastminute.user.external.dto;

import com.lastminute.user.domain.ForbiddenName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadForbiddenNameResponseDto {

    private final String name;
    private final String reason;

    public static ReadForbiddenNameResponseDto of(ForbiddenName entity) {
        return ReadForbiddenNameResponseDto.builder()
                .name(entity.getName())
                .reason(entity.getReason())
                .build();
    }
}
