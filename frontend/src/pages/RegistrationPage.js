import React, { useContext, useState, useEffect } from 'react';
import '../styles/index.scss';
import '../styles/authForm.scss';
import { useHistory } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Toast } from '../components/toast/Toast';
import { ToastType } from '../components/toast/ToastType';
import $ from 'jquery';
import { useAccount } from '../hooks/api/account.hook';

export const RegistrationPage = () => {
    const history = useHistory();
    const { register } = useAccount();

    const username = React.createRef();
    const password = React.createRef();
    const repeatPassword = React.createRef();

    //todo: change this shit on context errors
    const [errorToast, setErrorToast] = useState(null);

    function inputChangeHandler(e) {
        // some front-end validation here
    }

    const authSubmitBtnHandler = async (e) => {
        e.preventDefault();

        try {
            const response = await register({
                username: username.current.value,
                password: password.current.value,
                repeatPassword: repeatPassword.current.value,
            });
            // history.push("/profile");
            history.go();
        } catch (e) {
            setErrorToast(<Toast type={ToastType.ERROR} header={`Error`} body={e.message} />);
            $(`.${ToastType.ERROR}`).toast('show');
        }
    };

    return (
        <div className="content-block-main">
            <form className="authForm">
                <h2>Welcome registration page.</h2>

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
                <input
                    ref={repeatPassword}
                    className="authField"
                    name="repeatPassword"
                    type="password"
                    placeholder="Repeat password"
                    onChange={inputChangeHandler}
                />
                <br />
                <button className="authSubmitBtn" onClick={authSubmitBtnHandler}>
                    Register
                </button>
                <br />
                <p>
                    If you already has an account, you can <a href="/login">login</a>
                </p>
            </form>
            {errorToast && errorToast}
        </div>
    );
};
