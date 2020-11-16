import { string } from 'prop-types';
import { useHttpRequest } from '../../api/request/httpRequest.hook';
import { M_PUBLICATIONS } from '../../constants/mappings';

export const usePublications = () => {

    const {httpRequest} = useHttpRequest();

    const getPublications = async ({ page = 0, howMuch = 10 }) => {
        try {
            const response = await httpRequest({
                url: `${M_PUBLICATIONS}/news?page=${page}&howMuch=${howMuch}`,
                method: 'GET',
            });

            return response;
        } catch (e) {
            throw new Error(e);
        }
    };

    const getUserPublications = async ({ page = 0, howMuch = 10, userId }) => {
        try {
            let url = `${M_PUBLICATIONS}/userNews?page=${page}&howMuch=${howMuch}`;
            if (userId) {
                url += `&userId=${userId}`;
            }

            console.log(url);

            const response = await httpRequest({
                url,
                method: 'GET',
            });

            return response;
        } catch (e) {
            throw new Error(e);
        }
    };

    const postPublication = async ({ theme, text }) => {
        const response = httpRequest({
            url: `${M_PUBLICATIONS}/makePublication`,
            method: 'POST',
            body: {
                theme: theme,
                text: text,
            },
        });

        return response;
    };

    return { getPublications, getUserPublications, postPublication };
};
