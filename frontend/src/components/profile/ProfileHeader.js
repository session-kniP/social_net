import React, { useEffect, useState } from 'react';
import { Modal } from '../Modal';
import Avatar from '../Avatar';
import AdditionalInfo from './AdditionalInfo';
import { UserCommunityStatus } from '../community/UserCommunityStatus';
import { useHttpRequest } from '../../api/request/httpRequest.hook';
import { Link } from 'react-router-dom';
import { ChatType } from '../../components/chat/ChatType';
import { M_COMMUNITY } from '../../constants/mappings';
import { ButtonGroup, Button } from 'react-bootstrap';
import TextareaAutosize from 'react-textarea-autosize';
import '../../styles/publicationForm.scss';
import { useProfile } from '../../hooks/profile.hook';
import { stat } from 'fs';

const ProfileHeader = ({ user, isModalShow, onModalShow, onModalClose, onLoadAvatar, avatar, editable }) => {

    const [isInfoOpened, setIsInfoOpened] = useState(false);
    const [statusEditable, setStatusEditable] = useState(false);

    const [status, setStatus] = useState(user.userInfo.status);
    const [statusSaveable, setStatusSaveable] = useState(false);

    const statusRef = React.createRef();

    const { httpRequest } = useHttpRequest();
    const { editProfile } = useProfile();

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
        const response = await httpRequest({
            url: `${M_COMMUNITY}/subscribe`,
            method: 'POST',
            body: { id: user.id },
        });
        history.go();
    };

    const unsubscribeHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({
            url: `${M_COMMUNITY}/unsubscribe`,
            method: 'POST',
            body: { id: user.id },
        });
        history.go();
    };

    const acceptFriendRequestHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({
            url: `${M_COMMUNITY}/acceptFriendRequest`,
            method: 'POST',
            body: { id: user.id },
        });
        history.go();
    };

    const removeFriendHandler = async (e) => {
        e.preventDefault();
        const response = await httpRequest({
            url: `${M_COMMUNITY}/removeFriend`,
            method: 'POST',
            body: { id: user.id },
        });
        history.go();
    };

    const statusChangeEvent = () => {
        const newStatus = statusRef.current.value;
        if (newStatus !== status) {
            setStatusSaveable(true);
            setStatus(newStatus);
            return true;
        }
        return false;
    };

    const acceptStatusChange = async () => {
        try {
            const response = await editProfile({ status: status });
            setStatusSaveable(false);
            setStatusEditable(false);
        } catch (e) {
            console.error(e);
        }
    };

    const declineStatusChanges = () => {
        setStatus(user.userInfo.status);
        statusRef.current.value = user.userInfo.status;
        setStatusSaveable(false);
        setStatusEditable(false);
    };

    useEffect(() => {
        statusRef.current.addEventListener('click', () => setStatusEditable(true));
        statusRef.current.addEventListener('focusout', () => setStatusEditable(false));
    }, []);

    const switchLinkByStatus = (status) => {
        console.log(status);
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
                        <a className="link my-2" onClick={subscribeHandler}>
                            Add as friend
                        </a>
                    </div>
                );
        }
    };

    return (
        <div className="container mx-auto px-0">
            <div className="row justify-content-center min-vw-50 m-0">
                <div className="px-2 py-2 profile-user-image-panel m-0">
                    <Avatar onModalShow={onModalShow} src={avatar ? avatar : `../../../resources/images/default.jpg`} />

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
                            <a className="link upload-new-avatar my-2 text-center">
                                <label htmlFor="avatar-loader" className="my-0 mx-1">
                                    Upload new avatar
                                </label>
                            </a>
                            <a className="link edit-profile my-2 text-center" href="/editProfile">
                                Edit profile
                            </a>
                        </div>
                    ) : (
                        <>
                            {switchLinkByStatus(user.communityStatus)}
                            {user.id && (
                                <Link
                                    to={{ pathname: `/chat/${user.id}`, state: { type: ChatType.PRIVATE } }}
                                    className="link my-2 text-center"
                                >
                                    Open dialogue
                                </Link>
                            )}
                        </>
                    )}
                </div>
                <div className="profile-additional-info mx-0 px-1 px-md-3">
                    <div className="profile-name-view">
                        {user.userInfo.firstName != null && user.userInfo.lastName != null && (
                            <div>
                                <label className="py-0 my-0">
                                    {user.userInfo.firstName + ' ' + user.userInfo.lastName}
                                </label>
                            </div>
                        )}
                        <div>
                            <label className="profile-username">{user.username}</label>
                        </div>

                        <TextareaAutosize
                            ref={statusRef}
                            maxRows={3}
                            className="publication-form-text w-100 h6 mb-0"
                            readOnly={!statusEditable}
                            onClick={() => setStatusEditable(true)}
                            onFocusOut={() => {
                                if (statusSaveable) {
                                    setStatusEditable(false);
                                }
                            }}
                            placeholder={`Your status here...`}
                            onChange={() => statusChangeEvent()}
                        >
                            {status}
                        </TextareaAutosize>
                        {statusSaveable && (
                            <ButtonGroup size="sm" className="w-100 d-flex justify-content-end" horizontal>
                                <Button
                                    variant="primary"
                                    className="col-sm-12 col-lg-2 col-md-3 m-1"
                                    onClick={() => acceptStatusChange()}
                                >
                                    Save
                                </Button>
                                <Button
                                    variant="secondary"
                                    className="col-sm-12 col-lg-2 col-md-3 m-1"
                                    onClick={() => declineStatusChanges()}
                                >
                                    Decline
                                </Button>
                            </ButtonGroup>
                        )}
                    </div>

                    {isInfoOpened ? (
                        <div>
                            <label onClick={infoClosingHandler} className="profile-additional-info-control opened">
                                Hide additional info
                            </label>
                            <AdditionalInfo
                                baseClass="profile"
                                info={{
                                    email: user.userInfo.email,
                                    sex: user.userInfo.sex,
                                }}
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
        </div>
    );
};

export default ProfileHeader;
