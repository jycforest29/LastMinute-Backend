package com.lastminute.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountState {

    NORMAL("NORMAL", true),
    WITHDRAWN("WITHDRAWN", false);

    private final String value;
    private final boolean accessible;
}
