import React from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { M_PROFILE, M_COMMUNITY } from '../constants/mappings';
import { useImageLoader } from './imageLoader.hook';

export const useProfile = () => {
    const { httpRequest } = useHttpRequest();
    const { getAvatar } = useImageLoader();

    const getProfile = async ({ id }) => {
        try {
            const profileRequestUrl = id ? `${M_COMMUNITY}/user?id=${id}` : `${M_PROFILE}`;
            const responseData = await httpRequest({
                url: profileRequestUrl,
                method: 'GET',
            });
            //setUser(responseData);

            const responseAvatar = await getAvatar(id);

            let avatar;
            if (responseAvatar.size != 0) {
                avatar = URL.createObjectURL(responseAvatar);
                //setAvatar(avatar);
            }

            return { user: responseData, avatar };
        } catch (e) {
            throw new Error(e);
        }
    };

    const editProfile = async ({ firstName = null, lastName = null, sex = null, email = null, status = null }) => {
        try {
            const response = await httpRequest({
                url: `${M_PROFILE}/edit`,
                method: 'POST',
                body: {
                    userInfo: {
                        firstName: firstName,
                        lastName: lastName,
                        sex: sex,
                        email: email,
                        status: status,
                    },
                },
            });

            return response;
        } catch (e) {
            throw new Error(e);
        }
    };

    return { getProfile, editProfile };
};
