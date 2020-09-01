import React, { useState, useCallback } from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';

const useUser = () => {
    const { httpRequest } = useHttpRequest();

    const getUser = useCallback(async () => {
        try {
            const responseData = await httpRequest({ url: '/profile', method: 'GET' });
            console.log('User is', responseData);
            return responseData;
        } catch (e) {
            console.log('ERROR', e);
        }
    }, [httpRequest]);

    return { getUser };
};

export default useUser;
