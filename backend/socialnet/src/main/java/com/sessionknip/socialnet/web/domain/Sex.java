package com.sessionknip.socialnet.web.domain;

import javax.persistence.Entity;


public enum Sex {
    MALE, FEMALE, UNDEFINED;

    @Override
    public String toString() {
        return name();
    }
}
