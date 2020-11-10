import React, { useCallback } from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { RequestDataType } from '../api/request/RequestDataType';
import { M_COMMUNITY, M_PROFILE } from '../constants/mappings';

export const useImageLoader = () => {
    const { httpRequest } = useHttpRequest();

    const getImageLink = (a, itemName = 'avatar') => {
        const image = a.childNodes.item(itemName);
        if (image) {
            return image.src;
        }
        return null;
    };

    const uploadAvatar = async (avatar) => {
        const formData = new FormData();

        formData.append('avatar', avatar);
        formData.append('title', 'Some Image Title Tho');

        try {
            const response = await httpRequest({
                url: `${M_PROFILE}/uploadAvatar`,
                method: 'POST',
                body: formData,
                type: RequestDataType.FORM_DATA,
            });

            history.go();
        } catch (e) {
            throw new Error(e.message);
        }
    };

    const getAvatar = useCallback(async (id) => {
        const url = id ? `${M_PROFILE}/getAvatar?id=${id}` : `${M_PROFILE}/getAvatar`;

        try {
            return await getImage(url);
        } catch (e) {
            throw new Error(e.message);
        }
    }, []);

    const getCommunityPic = useCallback(async (id) => {
        const url = id ? `${M_COMMUNITY}/getCommunityPic?id=${id}` : null;

        if (!url) {
            throw new Error('Unknown community id');
        }

        try {
            return await getImage(url);
        } catch (e) {
            throw new Error(e.message);
        }
    });

    const getImage = useCallback(async (url) => {
        console.log(url);
        try {
            const responseAvatar = await httpRequest({
                url: url,
                method: 'GET',
                type: RequestDataType.IMAGE_JPEG,
            });

            console.log(responseAvatar)

            return responseAvatar;
        } catch (e) {
            throw new Error(e);
        }
    }, []);

    return { getImageLink, uploadAvatar, getAvatar, getImage, getCommunityPic };
};
