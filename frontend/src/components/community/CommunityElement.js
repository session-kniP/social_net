import React, { useState, useEffect, useCallback } from 'react';
import { useHistory } from 'react-router-dom';
import { CommunityPageMode } from './CommunityPageMode';
import { useCommunity } from '../../hooks/community.hook';
import { useImageLoader } from '../../hooks/imageLoader.hook';
import { DEFAULT_PROFILE_PIC } from '../../constants/constants';

import '../../styles/profile.scss';
import { M_PROFILE } from '../../constants/mappings';

export const CommunityElement = ({
    id,
    firstName,
    lastName,
    title, //for user is username
    mode = CommunityPageMode.FRIENDS,
    onDelete,
}) => {
    const history = useHistory();
    const {
        subscribe,
        unsubscribe,
        acceptFriendRequest,
        removeFriend,
    } = useCommunity();

    const [avatar, setAvatar] = useState(null);
    const { getAvatar, getCommunityPic } = useImageLoader();

    const acceptFriendRequestHandler = async (id) => {
        try {
            const response = await acceptFriendRequest(id);
            onDelete(id);
        } catch (e) {
            console.error(e.cause.message);
        }
    };

    const subscribtionHandler = async (id) => {
        try {
            const response = await subscribe(id);
            onDelete(id);
        } catch (e) {
            console.error(e.cause.message);
        }
    };

    const unsubscriptionHandler = async (id) => {
        try {
            console.log('UNSUB')
            const response = await unsubscribe(id);
            onDelete(id);
        } catch (e) {
            console.error(e.cause.message);
        }
    };

    const removeFriendHandler = async (id) => {
        try {
            const response = await removeFriend(id);
            onDelete(id);
        } catch (e) {
            console.error(e.cause.message);
        }
    };

    useEffect(() => {
        async function loadAvatar() {
            try {
                const responseAvatar = await getAvatar(id);
                if (responseAvatar.size && responseAvatar.size != 0) {
                    const avatar = global.URL.createObjectURL(responseAvatar);
                    setAvatar(avatar);
                }
            } catch (e) {
                console.error(e);
            }
        }
        loadAvatar();
    }, [getAvatar]);

    const elementOfMode = (mode) => {
        switch (mode) {

            case CommunityPageMode.COMMUNITIES:
                return( 
                <div className="">

                </div>)


            case CommunityPageMode.SUBSCRIBERS:
                return (
                    <div className="community-descision mx-0 text-center h-100 align-center">
                        <a
                            className="link community-link accept d-inline-block"
                            onClick={() => acceptFriendRequestHandler(id)}
                        >
                            Accept friend request
                        </a>
                    </div>
                );

            case CommunityPageMode.SUBSCRIPTIONS:
                return (
                    <div className="community-descision mx-0 text-center">
                        {console.log('THIS IS SUBSCRIPTIONS')}
                        <a
                            className="link community-link decline"
                            onClick={() => unsubscriptionHandler(id)}
                        >
                            Unsubscribe
                        </a>
                    </div>
                );

            case CommunityPageMode.FRIENDS:
                return (
                    <div className="community-descision mx-0 text-center">
                        <a
                            className="link community-link decline"
                            onClick={() => removeFriendHandler(id)}
                        >
                            Remove friend
                        </a>
                    </div>
                );

            case CommunityPageMode.ALL:
                return (
                    <div className="community-descision mx-0 text-center">
                        <a
                            className="link community-link accept"
                            onClick={() => subscribtionHandler(id)}
                        >
                            Add as friend
                        </a>
                    </div>
                );
        }
    };

    return (
        <div className="community-element row" key={id}>
            <div className="profile-user-image-panel m-0 p-0">
                <div
                    className="community-avatar"
                    style={{
                        backgroundImage: `url(${
                            avatar ? avatar : DEFAULT_PROFILE_PIC
                        })`,
                    }}
                ></div>
            </div>
            <div className="profile-user-info-panel">
                <div className="community-info mx-lg-2 mx-1">
                    {firstName && lastName && (
                        <a href={'/profile/' + id}>
                            <label className="profile-link">
                                {firstName + ' ' + lastName}
                            </label>
                        </a>
                    )}
                    <br />
                    <a href={'/profile/' + id}>
                        <label className="profile-username profile-link">
                            {title}
                        </label>
                    </a>
                </div>
                {elementOfMode(mode)}
            </div>
        </div>
    );
};
