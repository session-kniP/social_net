import React, { useContext } from 'react';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import '../styles/index.css';
import '../styles/authForm.css';
import { useHistory } from 'react-router-dom';
import { useAuth } from '../../src/hooks/auth.hook';
import { AuthContext } from '../context/AuthContext';
import {RequestDataType} from '../api/request/RequestDataType';

export default () => {
    const { httpRequest } = useHttpRequest();
    const { login } = useContext(AuthContext);
    const history = useHistory();

    let username = React.createRef();
    let password = React.createRef();

    function inputChangeHandler(e) {
        // some front-end validation here
    }

    function clearForm() {
        username.current.innerText = '';
        password.current.innerText = '';
    }

    async function authSubmitBtnHandler(e) {
        try {
            const responseData = await httpRequest({
                url: '/api/auth/login',
                method: 'POST',
                body: { username: username.current.value, password: password.current.value },
                type: RequestDataType.JSON
            });

            console.log('RESPONSE DATA', responseData);
            login(responseData.token, responseData.id);

            // history.push("/profile");
            history.go();
        } catch (e) {
            clearForm();

            console.error(e);
        }
    }

    return (
        <div className="content-block-main">
            <div className="authForm">
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
                <button className="authSubmitBtn" onClick={authSubmitBtnHandler}>
                    Login
                </button>
                <br />
                <p>
                    If you don't have an account yet, you can <a href="/registration">register</a> it
                </p>
            </div>
        </div>
    );
};

// class LoginPage extends React.Component {
//     constructor(props) {
//         super(props);

//         this.changeHandler = this.changeHandler.bind(this);
//         this.performAuthorization = this.performAuthorization.bind(this);
//         // this.

//         this.state = {
//             username: '',
//             password: '',
//         };
//     }

//     changeHandler(event) {
//         this.setState({ [event.target.name]: event.target.value });
//     }

//     performAuthorization() {
//         const request = new Request();

//         try {
//             const data = request.request({url: '/api/auth/login', method: 'POST', body: { ...this.state }});
//             console.log(request.state.loading);
//             console.log('Data is', data);
//         } catch (e) {
//             console.error(e);
//         }
//     }

//     render() {
//         return (
//             <div>
//                 <h1>Hi, I'm Login Page!!!</h1>
//                 <div className="authField">
//                     <input
//                         placeholder="Username"
//                         id="username"
//                         name="username"
//                         type="text"
//                         onChange={this.changeHandler}
//                     />
//                     <label htmlFor="username"></label>
//                 </div>

//                 <div className="authField">
//                     <input
//                         placeholder="Password"
//                         id="password"
//                         name="password"
//                         type="password"
//                         onChange={this.changeHandler}
//                     />
//                     <label htmlFor="password"></label>
//                 </div>

//                 <div className="authBtn">
//                     <button className="performAuthBtn" onClick={this.performAuthorization}>
//                         Authorize
//                     </button>
//                 </div>
//             </div>
//         );
//     }
// }

// export default LoginPage;
