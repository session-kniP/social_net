import React, { useState, useCallback } from 'react';
import {TOKEN_NAME, USER_ID} from '../constants/constants';

export const useAuth = () => {    
    const login = useCallback((token, user_id) => {
        localStorage.setItem(TOKEN_NAME, token);
        localStorage.setItem(USER_ID, user_id);
    }, []);

    const logout = useCallback(() => {
        localStorage.removeItem(TOKEN_NAME);
        localStorage.removeItem(USER_ID);
    }, []);

    const isToken = () => {
        return localStorage.getItem(TOKEN_NAME) ? true : false;
    }

    return { isToken, login, logout };
    // authorized: localStorage.getItem(TOKEN_NAME) ? true : false
};
