import React, { useState, useContext, useCallback, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ProfileHeader from '../components/profile/ProfileHeader';
import { UserContext } from '../dev/DevContext';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { USER_ID } from '../constants/constants';

import '../styles/index.scss';
import '../styles/profile.scss';
import { useImageLoader } from '../hooks/imageLoader.hook';
import { M_COMMUNITY, M_PROFILE } from '../constants/mappings';
import { useCommunity } from '../hooks/community.hook';
import { useAuth } from '../hooks/auth.hook';
import { constants } from 'fs';

export const ProfilePage = () => {
    const id = useParams().id;
    const [modalShow, setModalShow] = useState(false);
    const [avatarLink, setAvatarLink] = useState(null);

    const [user, setUser] = useState(null);
    const [avatar, setAvatar] = useState(null);
    const { httpRequest } = useHttpRequest();

    const { logout } = useAuth();

    const { getImageLink, uploadAvatar, getAvatar } = useImageLoader();

    const toastRef = React.createRef();

    const getUser = useCallback(async () => {
        try {
            const profileRequestUrl = id ? `${M_COMMUNITY}/user?id=${id}` : `${M_PROFILE}`;
            const responseData = await httpRequest({
                url: profileRequestUrl,
                method: 'GET',
            });
            setUser(responseData);

            const responseAvatar = await getAvatar(id);

            if (responseAvatar.size != 0) {
                const avatar = URL.createObjectURL(responseAvatar);

                setAvatar(avatar);
            }
        } catch (e) {
            console.error('ERROR', e);
            logout();
            history.go();
        }
    }, []);

    useEffect(() => {
        getUser();
    }, [getUser]);

    // const user = useContext(UserContext);
    const getParent = (e) => {
        return e.target.parentElement;
    };

    const showModal = (e) => {
        const parent = getParent(e);
        const link = getImageLink(parent, 'avatar');
        setModalShow(true);
        setAvatarLink(link);
    };

    const closeModal = (e) => {
        setModalShow(false);
    };

    return (
        <div className="content-block-main col-12 p-0 m-auto">
            {user && (
                <ProfileHeader
                    user={user}
                    isModalShow={modalShow}
                    onModalShow={showModal}
                    onModalClose={closeModal}
                    onLoadAvatar={uploadAvatar}
                    avatar={avatar}
                    editable={user.id === localStorage.getItem(USER_ID) ? false : true}
                />
            )}
        </div>
    );
};
