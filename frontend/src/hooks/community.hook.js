import { useHttpRequest } from '../api/request/httpRequest.hook';

const communityMapping = '/api/community';

export const useCommunity = () => {
    const { httpRequest } = useHttpRequest();

    const communityRequest = async (mapping, id) => {
        try {
            const response = await httpRequest({
                url: `${communityMapping}/${mapping}`,
                method: 'POST',
                body: { id: id },
            });

            return response;
        } catch (e) {
            throw new Error("Can't execute community request request", e);
        }
    };

    const subscribe = (id) => {
        try {
            const response = communityRequest('subscribe', id);

            return response;
        } catch (e) {
            throw new Error("Can't subscribe", e);
        }
    };

    const unsubscribe = (id) => {
        try {
            const response = communityRequest('unsubscribe', id);

            return response;
        } catch (e) {
            throw new Error("Can't unsubscribe", e);
        }
    };

    const acceptFriendRequest = async (id) => {
        try {
            const response = communityRequest('acceptFriendRequest', id);

            return response;
        } catch (e) {
            throw new Error("Can't accept friend request", e);
        }
    };

    const removeFriend = (id) => {
        try {
            const response = communityRequest('removeFriend', id);

            return response;
        } catch (e) {
            throw new Error("Can't remove a friend", e);
        }
    };

    return { subscribe, unsubscribe, acceptFriendRequest, removeFriend };
};
