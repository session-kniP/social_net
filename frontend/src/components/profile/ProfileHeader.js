import React, { useEffect, useState } from 'react';
import { Modal } from '../Modal';
import Avatar from '../Avatar';
import AdditionalInfo from './AdditionalInfo';
import { UserCommunityStatus } from '../community/UserCommunityStatus';
import { useHttpRequest } from '../../api/request/httpRequest.hook';

const communityUrl = '/api/community';

const ProfileHeader = ({ user, isModalShow, onModalShow, onModalClose, onLoadAvatar, avatar, editable }) => {
    const [isInfoOpened, setIsInfoOpened] = useState(false);

    const { httpRequest } = useHttpRequest();

    const infoClosingHandler = () => {
        setIsInfoOpened(false);
    };

    const infoOpeningHandler = () => {
        setIsInfoOpened(true);
    };

    const fileLoadedHandler = (e) => {
        if (e.target.value) {
            onLoadAvatar(e.target.files[0]);
        }
    };

    const subscribeHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({ url: communityUrl + '/subscribe', method: 'POST', body: { id: user.id } });
        history.go();
    };

    const unsubscribeHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({ url: communityUrl + '/unsubscribe', method: 'POST', body: { id: user.id } });
        history.go();
    };

    const acceptFriendRequestHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({
            url: communityUrl + '/acceptFriendRequest',
            method: 'POST',
            body: { id: user.id },
        });
        history.go();
    };

    const removeFriendHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({
            url: communityUrl + '/removeFriend',
            method: 'POST',
            body: { id: user.id },
        });
        history.go();
    };

    const switchLinkByStatus = (status) => {
        switch (status) {
            case UserCommunityStatus.SUBSCRIPTION:
                return (
                    <div>
                        <label className="user-community-status">You subscribed to this user</label>
                        <a className="link unsubscribe" onClick={unsubscribeHandler}>
                            Unsubscribe
                        </a>
                    </div>
                );

            case UserCommunityStatus.SUBSCRIBER:
                return (
                    <div>
                        <label className="user-community-status">This user subscribed to you</label>
                        <a className="link accept-friend-request" onClick={acceptFriendRequestHandler}>
                            Accept friend request
                        </a>
                    </div>
                );

            case UserCommunityStatus.FRIEND:
                return (
                    <div>
                        <label className="user-community-status">This user is your friend</label>
                        <a className="link remove-friend" onClick={removeFriendHandler}>
                            Remove friend
                        </a>
                    </div>
                );

            case UserCommunityStatus.NONE:
                return (
                    <div>
                        <a className="link none" onClick={subscribeHandler}>
                            Add as friend
                        </a>
                    </div>
                );
        }
    };

    return (
        <div className="profile-wrapper">
            <div className="profile-user-image-panel">
                {avatar && <Avatar onModalShow={onModalShow} src={avatar} />}

                <Modal
                    isOpened={isModalShow}
                    onClose={onModalClose}
                    children={
                        <img
                            className="modal-image"
                            src={avatar ? avatar : '../../resources/images/default.jpg'}
                            onErrorCapture={(e) => (e.target.src = '../../resources/images/default.jpg')}
                        />
                    }
                />

                {editable ? (
                    <div>
                        <input id="avatar-loader" type="file" onChange={fileLoadedHandler} hidden={true}></input>
                        <a className="link upload-new-avatar">
                            <label htmlFor="avatar-loader">Upload new avatar</label>
                        </a>
                        <a className="link edit-profile" href="/editProfile">
                            Edit profile
                        </a>
                    </div>
                ) : (
                    switchLinkByStatus(user.communityStatus)
                )}
            </div>
            <div className="profile-user-info-panel">
                <div className="profile-name-view">
                    {user.userInfo.firstName != null && user.userInfo.lastName != null && (
                        <>
                            <label className="profile-full-name">
                                {user.userInfo.firstName + ' ' + user.userInfo.lastName}
                            </label>
                            <br />
                        </>
                    )}

                    <label className="profile-username">{user.username}</label>
                </div>

                {isInfoOpened ? (
                    <div>
                        <label onClick={infoClosingHandler} className="profile-additional-info-control opened">
                            Hide additional info
                        </label>
                        <AdditionalInfo
                            baseClass="profile"
                            info={{ email: user.userInfo.email, sex: user.userInfo.sex }}
                        />
                    </div>
                ) : (
                    <div>
                        <label onClick={infoOpeningHandler} className="profile-additional-info-control closed">
                            Show additional info
                        </label>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfileHeader;
