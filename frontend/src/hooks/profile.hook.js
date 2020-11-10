import React from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { M_PROFILE } from '../constants/mappings';

export const useProfile = () => {
    
    const {httpRequest} = useHttpRequest();
    
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
                        status: status
                    }
                }
            });

            return response;
        } catch(e) {
            throw new Error(e);
        }
        

    };

    return {editProfile}
};
