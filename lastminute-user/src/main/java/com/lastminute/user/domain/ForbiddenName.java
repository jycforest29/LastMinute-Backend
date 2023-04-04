package com.lastminute.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "forbidden_names")
public class ForbiddenName {

    @Id
    private String name;

    @NotNull
    @Column(length = 30)
    private String reason;

    @Builder
    public ForbiddenName(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }
}
