import React, { useState, useContext, useCallback, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ProfileHeader from '../components/profile/ProfileHeader';
import { UserContext } from '../dev/DevContext';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { RequestDataType } from '../api/request/RequestDataType';

import '../styles/index.css';
import '../styles/profile.css';

export const ProfilePage = () => {
    const id = useParams().id;
    const [modalShow, setModalShow] = useState(false);
    const [avatarLink, setAvatarLink] = useState(null);

    const [user, setUser] = useState(null);
    const [avatar, setAvatar] = useState(null);
    const { httpRequest } = useHttpRequest();

    const getUser = useCallback(async () => {
        try {
            const profileRequestUrl = id ? `/api/community/user?id=${id}` : '/profile';
            const responseData = await httpRequest({ url: profileRequestUrl, method: 'GET' });
            setUser(responseData);

            const avatarRequestUrl = id ? `/profile/getAvatar?id=${id}` : '/profile/getAvatar';
            const responseAvatar = await httpRequest({
                url: avatarRequestUrl,
                method: 'GET',
                type: RequestDataType.IMAGE_JPEG,
            });

            setAvatar(URL.createObjectURL(responseAvatar));

        } catch (e) {
            console.log('ERROR', e);
            history.go();
        }
    }, []);

    useEffect(() => {
        getUser();
        console.log('AVATAR IS', avatar);
    }, [getUser]);

    // const user = useContext(UserContext);
    const getParent = (e) => {
        return e.target.parentElement;
    };

    const getImageLink = (a) => {
        const image = a.childNodes.item('avatar');
        if (image) {
            return image.src;
        }
        return null;
    };

    const onLoadAvatar = async (avatar) => {
        const formData = new FormData();

        formData.append('avatar', avatar);
        formData.append('title', 'Some Image Title Tho');

        try {
            const response = await httpRequest({
                url: '/profile/loadAvatar',
                method: 'POST',
                body: formData,
                type: RequestDataType.FORM_DATA,
            });

            history.go();
        } catch (e) {
            console.log(e);
        }
    };

    const showModal = (e) => {
        const parent = getParent(e);
        const link = getImageLink(parent);
        setModalShow(true);
        setAvatarLink(link);
    };

    const closeModal = (e) => {
        setModalShow(false);
    };

    return (
        <div className="content-block-main">
            {id
                ? user && (
                      <ProfileHeader
                          user={user}
                          isModalShow={modalShow}
                          onModalShow={showModal}
                          onModalClose={closeModal}
                          onLoadAvatar={onLoadAvatar}
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
                          onLoadAvatar={onLoadAvatar}
                          avatar={avatar}
                          editable={true}
                      />
                  )}
        </div>
    );
};
