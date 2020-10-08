import React, { useContext, useState, useEffect } from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import '../styles/index.scss';
import '../styles/authForm.scss';
import { useHistory } from 'react-router-dom';
import { useAuth } from '../../src/hooks/auth.hook';
import { AuthContext } from '../context/AuthContext';
import { RequestDataType } from '../api/request/RequestDataType';
import { Toast } from '../components/toast/Toast';
import { ToastType } from '../components/toast/ToastType';
import $ from 'jquery';
import { M_AUTH } from '../constants/mappings';

export default () => {
    const { httpRequest } = useHttpRequest();
    const { login } = useContext(AuthContext);
    const history = useHistory();

    const username = React.createRef();
    const password = React.createRef();

    const [errorToast, setErrorToast] = useState(null);

    function inputChangeHandler(e) {
        // some front-end validation here
    }

    const authSubmitBtnHandler = async (e) => {
        e.preventDefault();

        try {
            const responseData = await httpRequest({
                url: `${M_AUTH}/login`,
                method: 'POST',
                body: {
                    username: username.current.value,
                    password: password.current.value,
                },
                type: RequestDataType.JSON,
            });

            login(responseData.token, responseData.id);

            // history.push("/profile");
            history.go();
        } catch (e) {
            setErrorToast(
                <Toast
                    type={ToastType.ERROR}
                    header={`Error`}
                    body={e.message}
                />
            );
            $(`.${ToastType.ERROR}`).toast('show');
        }
    }

    return (
        <div className="content-block-main">
            <form className="authForm">
                <h2>Welcome login page.</h2>
                <h3>Please, authorize or create an account</h3>

                <input
                    ref={username}
                    className="authField"
                    name="username"
                    type="text"
                    placeholder="Username"
                    onChange={inputChangeHandler}
                />
                <br />
                <input
                    ref={password}
                    className="authField"
                    name="password"
                    type="password"
                    placeholder="Password"
                    onChange={inputChangeHandler}
                />
                <br />
                <button
                    className="authSubmitBtn"
                    onClick={authSubmitBtnHandler}
                >
                    Login
                </button>
                <br />
                <p>
                    If you don't have an account yet, you can{' '}
                    <a href="/registration">register</a> it
                </p>
            </form>
            {errorToast && errorToast}
        </div>
    );
};