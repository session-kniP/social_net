import React, { useState, useEffect, useCallback } from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { useHistory } from 'react-router-dom';
import { Sex } from '../components/profile/Sex';
import { M_PROFILE } from '../constants/mappings';
import {useAuth} from '../hooks/auth.hook';

import '../styles/index.scss';
import '../styles/profile.scss';
import '../styles/publicationForm.scss';
import { useProfile } from '../hooks/profile.hook';

export const EditProfilePage = () => {
    const [user, setUser] = useState(null);
    const { httpRequest } = useHttpRequest();
    const { editProfile } = useProfile();

    const {logout} = useAuth();

    const [state, setState] = useState(this);

    const [successMsg, setSuccessMsg] = useState(null);
    const [errorMsg, setErrorMsg] = useState(null);

    const firstNameRef = React.createRef();
    const lastNameRef = React.createRef();
    const emailRef = React.createRef();
    const sexRef = React.createRef();

    const history = useHistory();

    const testUser = {
        username: 'Chatsky',
        userInfo: {
            firstName: 'Максим',
            lastName: 'Чацкий',
            email: 'rabbitrus8@gmail.com',
            sex: 'MALE',
        },
    };

    const getUser = useCallback(async () => {
        try {
            const responseData = await httpRequest({
                url: M_PROFILE,
                method: 'GET',
            });
            console.log('User is', responseData);
            setUser(responseData);
        } catch (e) {
            console.log('ERROR', e);
            logout();
            history.go();
            // setUser(testUser);
        }
    }, []);

    useEffect(() => {
        getUser();
    }, [getUser]);

    const sendEdit = useCallback(async () => {
        try {
            const response = await editProfile({
                firstName: firstNameRef.current.value,
                lastName: lastNameRef.current.value,
                email: emailRef.current.value,
                sex: sexRef.current.options[sexRef.current.selectedIndex].value,
            });

            setSuccessMsg(response.message);
        } catch (e) {
            setErrorMsg(e.message);
        }
    });

    const declineEdit = () => {
        history.go();
    };

    return (
        <div className="content-block-main">
            {user && (
                <div className="profile table">
                    <h1 style={{ marginTop: 0 }}>Edit profile</h1>
                    <h2 style={{ marginBottom: 0 }} className="profile-username">
                        {user.username}
                    </h2>

                    <table className="profile table">
                        <tbody>
                            <tr className="profile table-row">
                                <td className="profile table-key">First name</td>
                                <td className="profile table-value">
                                    <input
                                        ref={firstNameRef}
                                        className="profile text-input"
                                        type="text"
                                        defaultValue={user.userInfo['firstName']}
                                    />
                                </td>
                            </tr>
                            <tr className="profile table-row">
                                <td className="profile table-key">Last name</td>
                                <td className="profile table-value">
                                    <input
                                        ref={lastNameRef}
                                        className="profile text-input"
                                        type="text"
                                        defaultValue={user.userInfo['lastName']}
                                    />
                                </td>
                            </tr>
                            <tr className="profile table-row">
                                <td className="profile table-key">Email</td>
                                <td className="profile table-value">
                                    <input
                                        ref={emailRef}
                                        className="profile text-input"
                                        type="text"
                                        defaultValue={user.userInfo['email']}
                                    />
                                </td>
                            </tr>
                            <tr className="profile table-row">
                                <td className="profile table-key">Sex</td>
                                <td className="profile table-value">
                                    <select
                                        ref={sexRef}
                                        className="profile text-input"
                                        type="mult"
                                        defaultValue={user.userInfo['sex']}
                                    >
                                        {Object.entries(Sex).map((sex, index) => {
                                            return (
                                                <option key={index} value={sex[0]}>
                                                    {sex[1]}
                                                </option>
                                            );
                                        })}
                                    </select>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <br />
                    <div>
                        <button className="publication-form-button send" onClick={sendEdit}>
                            Apply
                        </button>
                        <button className="publication-form-button decline" onClick={declineEdit}>
                            Decline all
                        </button>
                        <label className="success-msg" hidden={successMsg ? false : true}>
                            {successMsg}
                        </label>
                        <label className="error-msg" hidden={errorMsg ? false : true}>
                            {errorMsg}
                        </label>
                    </div>
                    <br />
                </div>
            )}
        </div>
    );
};
