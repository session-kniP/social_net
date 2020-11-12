import React, { useState, useContext, useCallback, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ProfileHeader from '../components/profile/ProfileHeader';
import { USER_ID } from '../constants/constants';
import { useProfile } from '../hooks/profile.hook';

import '../styles/index.scss';
import '../styles/profile.scss';
import { useImageLoader } from '../hooks/imageLoader.hook';
import { useAuth } from '../hooks/auth.hook';
import { usePublications } from '../hooks/publications.hook';
import { Publication } from '../components/Publication';

export const ProfilePage = () => {
    const id = useParams().id;
    const [modalShow, setModalShow] = useState(false);
    const [avatarLink, setAvatarLink] = useState(null);

    const [user, setUser] = useState(null);
    const [avatar, setAvatar] = useState(null);
    const [publications, setPublications] = useState(null);

    const { logout } = useAuth();

    const { getProfile } = useProfile();
    const { getUserPublications } = usePublications();

    const { getImageLink, uploadAvatar } = useImageLoader();

    const toastRef = React.createRef();

    const getUser = useCallback(async () => {
        try {
            const { user, avatar } = await getProfile({ id });

            setUser(user);
            setAvatar(avatar);
        } catch (e) {
            console.error('ERROR', e);
            logout();
            history.go();
        }
    }, []);

    const loadPublications = useCallback(async () => {
        try {
            const response = await getUserPublications({ userId: id });
            setPublications(response);
        } catch (e) {
            console.error(e);
            logout();
            history.go();
        }
    }, []);

    useEffect(() => {
        getUser();
        loadPublications();
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
                    editable={user.id == localStorage.getItem(USER_ID) ? true : false}
                />
            )}
            <div className="content-block-publications mx-auto col-12 col-md-8 col-lg-8">
                {publications &&
                    publications.map((p) => {
                        return <Publication props={p} key={Math.random()} />;
                    })}
            </div>
        </div>
    );
};
