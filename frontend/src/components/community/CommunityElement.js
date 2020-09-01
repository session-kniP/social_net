import React from 'react';
import { useHistory } from 'react-router-dom';
import { CommunityPageMode } from '../../pages/CommunityPage';
import { useCommunity } from '../../hooks/community.hook';

export const CommunityElement = ({ id, firstName, lastName, title, mode = CommunityPageMode.FRIENDS, onDelete }) => {
    const history = useHistory();
    const { subscribe, unsubscribe, acceptFriendRequest, removeFriend } = useCommunity();

    const acceptFriendRequestHandler = async (id) => {
        try {
            const request = await acceptFriendRequest(id);
            onDelete(id);
        } catch (e) {
            console.error(e);
        }
    };

    const subscribtionHandler = async (id) => {
        try {
            const request = await subscribe(id);
            onDelete(id);
        } catch (e) {
            console.error(e);
        }
    };

    const unsubscriptionHandler = async (id) => {
        try {
            const request = await unsubscribe(id);
            onDelete(id);
        } catch (e) {
            console.error(e);
        }
    };

    const removeFriendHandler = async (id) => {
        try {
            const request = await removeFriend(id);
            onDelete(id);
        } catch (e) {
            console.error(e);
        }
    };

    const elementOfMode = (mode) => {
        switch (mode) {
            case CommunityPageMode.SUBSCRIBERS:
                return (
                    <div className="community-descision">
                        <a className="link community-link accept" onClick={() => acceptFriendRequestHandler(id)}>
                            Accept friend request
                        </a>
                    </div>
                );

            case CommunityPageMode.SUBSCRIPTIONS:
                return (
                    <div className="community-descision">
                        <a className="link community-link decline" onClick={() => unsubscriptionHandler(id)}>
                            Unsubscribe
                        </a>
                    </div>
                );

            case CommunityPageMode.FRIENDS:
                return (
                    <div className="community-descision">
                        <a className="link community-link decline" onClick={() => removeFriendHandler(id)}>
                            Remove friend
                        </a>
                    </div>
                );

            case CommunityPageMode.ALL:
                return (
                    <div className="community-descision">
                        <a className="link community-link accept" onClick={() => subscribtionHandler(id)}>
                            Add as friend
                        </a>
                    </div>
                );
        }
    };

    return (
        <div className="community-element" key={id}>
            <div className="profile-user-image-panel">
                <img className="community-avatar" src="../../resources/images/default.jpg"></img>
            </div>
            <div className="profile-user-info-panel">
                <div className="community-info">
                    {firstName && lastName && (
                        <a href={'/profile/' + id}>
                            <label className="profile-link">{firstName + ' ' + lastName}</label>
                        </a>
                    )}
                    <br />
                    <a href={'/profile/' + id}>
                        <label className="profile-username profile-link">{title}</label>
                    </a>
                </div>
                {elementOfMode(mode)}
            </div>
        </div>
    );
};
