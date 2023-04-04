package com.lastminute.user.external.dto;

import com.lastminute.user.domain.ForbiddenName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateForbiddenNameRequestDto {

    @NotNull
    @Size(max = 12)
    private final String name;

    @NotNull
    @Size(max = 30)
    private final String reason;

    public ForbiddenName toEntity() {
        return ForbiddenName.builder()
                .name(this.name)
                .reason(this.reason)
                .build();
    }
}
