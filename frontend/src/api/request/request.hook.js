import React, { useState, useCallback } from 'react';
import { REMOTE } from '../../constants/constants';
import { RequestDataType } from './RequestDataType';

export const useRequest = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const request = useCallback(async ({ url, method = 'GET', headers = {}, body = null, type }) => {
        setLoading(true);
        try {
            (body && type == RequestDataType.JSON) ? (body = JSON.stringify(body)) : null;

            switch (type) {
                case RequestDataType.JSON:
                    headers['Content-Type'] = 'application/json';
                    headers['Accept'] = 'application/json';
                    break;

                case RequestDataType.FORM_DATA:
                    // headers['Content-Type'] = undefined;
                    // headers['Process-Data'] = undefined;
                    break;

                case RequestDataType.IMAGE_JPEG:
                    headers['Content-Type'] = 'image/jpeg';
                    break;
            }

            headers['Access-Control-Allow-Origin'] = '**';
            headers['Access-Control-Allow-Credentials'] = true;

            const dataResponse = await fetch(REMOTE + url, { method, headers, body, credentials: 'include' });

            const response = type === RequestDataType.JSON ? dataResponse.json() : dataResponse;
            const httpStatus = dataResponse.status;

            return { response: response, httpStatus: httpStatus };
        } catch (e) {
            throw e;
        } finally {
            setLoading(false);
        }
    }, []);

    const clearError = () => {
        setError(null);
    };

    return { request };
};
