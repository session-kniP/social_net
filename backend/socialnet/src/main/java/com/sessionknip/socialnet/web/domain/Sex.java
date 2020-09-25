package com.sessionknip.socialnet.web.domain;

public enum Sex {
    MALE, FEMALE, UNDEFINED;

    @Override
    public String toString() {
        return name();
    }
}
