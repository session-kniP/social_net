import React, { useCallback } from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { RequestDataType } from '../api/request/RequestDataType';

export const useAvatar = () => {
    const { httpRequest } = useHttpRequest();

    const getImageLink = (a, itemName = 'avatar') => {
        const image = a.childNodes.item(itemName);
        if (image) {
            return image.src;
        }
        return null;
    };

    const loadAvatar = async (avatar) => {
        const formData = new FormData();

        formData.append('avatar', avatar);
        formData.append('title', 'Some Image Title Tho');

        try {
            const response = await httpRequest({
                url: '/profile/loadAvatar',
                method: 'POST',
                body: formData,
                type: RequestDataType.FORM_DATA,
            });

            history.go();
        } catch (e) {
            throw new Error(e.message);
        }
    };

    const getAvatar = useCallback(async (url) => {
        try {
            const responseAvatar = await httpRequest({
                url: url,
                method: 'GET',
                type: RequestDataType.IMAGE_JPEG,
            });

            return responseAvatar;
        } catch (e) {
            throw new Error(e.message);
        }
    }, []);

    return { getImageLink, loadAvatar, getAvatar };
};
