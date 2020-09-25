package com.sessionknip.socialnet.web.service;

public abstract class NullAndEmptyChecker {

    protected boolean notNullAndNotEmpty(String object) {
        return object != null && !object.isEmpty();
    }

    protected boolean nullOrEmpty(String object) {
        return !notNullAndNotEmpty(object);
    }

}
