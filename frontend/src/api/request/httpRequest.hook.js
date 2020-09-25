import { useContext, useCallback } from 'react';
import { useRequest } from './request.hook';
import { AuthContext } from '../../context/AuthContext';
import { TOKEN_NAME } from '../../constants/constants';

import HttpStatus from 'http-status-codes';
import { RequestDataType } from './RequestDataType';
import { useHistory } from 'react-router-dom';

export const useHttpRequest = () => {
    const { request } = useRequest();
    const { logout } = useContext(AuthContext);
    const history = useHistory();

    const httpRequest = useCallback(
        async ({
            url,
            method = 'GET',
            headers = {},
            body = null,
            type = RequestDataType.JSON,
        }) => {
            try {
                const token = localStorage.getItem(TOKEN_NAME);

                if (token) {
                    headers['Authorization'] = 'Bearer_' + token;
                }

                const { response, httpStatus } = await request({
                    url,
                    method,
                    headers,
                    body,
                    type,
                });

                if (httpStatus === HttpStatus.UNPROCESSABLE_ENTITY) {
                    throw new Error(response.message);
                }

                if (httpStatus === HttpStatus.FORBIDDEN) {
                    if (!localStorage.getItem(TOKEN_NAME)) {
                        logout();
                        history.go();
                    }
                    throw new Error(response.message);
                }

                if (httpStatus === HttpStatus.INTERNAL_SERVER_ERROR) {
                    logout();
                    history.go();
                    throw new Error('Server error. Please, try again later');
                }

                return type === RequestDataType.IMAGE_JPEG
                    ? response.blob()
                    : response;
            } catch (e) {
                logout();
                history.push('/error/503');     //todo replace 503, make 'Ooops' error

                throw new Error(e.message);
            }
        },
        []
    );

    const formDataRequest = useCallback(async ({}) => {
        {
            url, (method = 'GET'), (headers = {}), (body = null);
        }
    });

    return { httpRequest };
};
