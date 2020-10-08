import React, { useState, useContext, useCallback, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ProfileHeader from '../components/profile/ProfileHeader';
import { UserContext } from '../dev/DevContext';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import $ from 'jquery';

import '../styles/index.scss';
import '../styles/profile.scss';
import { useAvatar } from '../hooks/avatar.hook';
import { M_COMMUNITY, M_PROFILE } from '../constants/mappings';
import { useCommunity } from '../hooks/community.hook';

export const ProfilePage = () => {
    const id = useParams().id;
    const [modalShow, setModalShow] = useState(false);
    const [avatarLink, setAvatarLink] = useState(null);

    const [user, setUser] = useState(null);
    const [avatar, setAvatar] = useState(null);
    const { httpRequest } = useHttpRequest();

    const { getImageLink, loadAvatar, getAvatar } = useAvatar();

    const toastRef = React.createRef();

    const getUser = useCallback(async () => {
        try {
            const profileRequestUrl = id
                ? `${M_COMMUNITY}/user?id=${id}`
                : `${M_PROFILE}`;
            const responseData = await httpRequest({
                url: profileRequestUrl,
                method: 'GET',
            });
            setUser(responseData);

            const avatarRequestUrl = id
                ? `${M_PROFILE}/getAvatar?id=${id}`
                : `${M_PROFILE}/getAvatar`;

            const responseAvatar = await getAvatar(avatarRequestUrl);

            if (responseAvatar.size != 0) {
                const avatar = URL.createObjectURL(responseAvatar);
                setAvatar(avatar);
            }
        } catch (e) {
            console.log('ERROR', e);
            // history.go();
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
            {id
                ? user && (
                      <ProfileHeader
                          user={user}
                          isModalShow={modalShow}
                          onModalShow={showModal}
                          onModalClose={closeModal}
                          onLoadAvatar={loadAvatar}
                          avatar={avatar}
                          editable={false}
                      />
                  )
                : user && (
                      <ProfileHeader
                          user={user}
                          isModalShow={modalShow}
                          onModalShow={showModal}
                          onModalClose={closeModal}
                          onLoadAvatar={loadAvatar}
                          avatar={avatar}
                          editable={true}
                      />
                  )}
        </div>
    );
};
