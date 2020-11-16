import { useHttpRequest } from '../../api/request/httpRequest.hook';
import { M_COMMUNITY } from '../../constants/mappings';
import { ChainException } from '../../exception/ChainException';

export const useCommunity = () => {
    const { httpRequest } = useHttpRequest();

    const communityRequest = async (mapping, id) => {
        try {
            const response = await httpRequest({
                url: `${M_COMMUNITY}/${mapping}`,
                method: 'POST',
                body: { id: id },
            });

            return response;
        } catch (e) {
            throw new ChainException({message: "Can't execute community request", cause: e});
        }
    };

    const subscribe = (id) => {
        try {
            const response = communityRequest('subscribe', id);

            return response;
        } catch (e) {
            throw new ChainException({message: "Can't subscribe", cause: e});
        }
    };

    const unsubscribe = (id) => {
        try {
            console.log('UNSUBSCRIPTION')
            const response = communityRequest('unsubscribe', id);

            return response;
        } catch (e) {
            throw new ChainException({message: "Can't unsubscribe", cause: e});
        }
    };

    const acceptFriendRequest = async (id) => {
        try {
            const response = communityRequest('acceptFriendRequest', id);

            return response;
        } catch (e) {
            throw new ChainException({message: "Can't accept friend request", cause: e});
        }
    };

    const removeFriend = (id) => {
        try {
            const response = communityRequest('removeFriend', id);

            return response;
        } catch (e) {
            throw new ChainException({message: "Can't remove a friend", cause: e});
        }
    };

    return { subscribe, unsubscribe, acceptFriendRequest, removeFriend };
};
