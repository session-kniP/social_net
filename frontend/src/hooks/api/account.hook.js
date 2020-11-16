import { useHttpRequest } from '../../api/request/httpRequest.hook';
import { M_AUTH } from '../../constants/mappings';

export const useAccount = () => {
    const { httpRequest } = useHttpRequest();

    const register = async ({ username, password, repeatPassword }) => {
        try {
            const result = await httpRequest({
                url: `${M_AUTH}/register`,
                method: 'POST',
                body: {
                    username: username,
                    password: password,
                    repeatPassword: repeatPassword,
                },
            });

            return result;
        } catch (e) {
            throw new Error(e);
        }
    };

    const login = async ({ username, password }) => {
        try {
            const result = await httpRequest({
                url: `${M_AUTH}/login`,
                method: 'POST',
                body: {
                    username: username,
                    password: password,
                },
            });

            return result;
        } catch (e) {
            throw new Error(e);
        }
    };

    return { register, login };
};
