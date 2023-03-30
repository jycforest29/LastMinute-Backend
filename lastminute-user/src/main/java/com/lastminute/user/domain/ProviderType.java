package com.lastminute.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {

    KAKAO("KAKAO");

    private final String value;
}
