package com.lastminute.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountState {

    NORMAL("NORMAL", true, true),
    SUSPENDED("SUSPENDED", true, false),
    WITHDRAWN("WITHDRAWN", false, false);

    private final String value;
    private final boolean visible;
    private final boolean accessible;
}
