import React from 'react';
import NavBar from './NavBar';
import { AuthContext } from '../context/AuthContext';
import { useContext } from 'react';
import { useHistory } from 'react-router-dom';

export const AuthorizedNavBar = () => {
    const history = useHistory();
    const { logout } = useContext(AuthContext);

    const logoutHandler = (event) => {
        event.preventDefault();
        logout();
        history.go();
        // history.push('/home');
    };

    return (
        <NavBar
            // className="authorized"
            mappingLinks={[
                { title: 'Home', href: '/profile' },
                { title: 'Feed', href: '/feed' },
                { title: 'Friends', href: '/friends' },
                { title: 'Subscribers', href: '/subscribers' },
            ]}
            additionalComps={[
                {
                    className: 'logout',
                    onClick: logoutHandler,
                    text: 'Log Out'
                },
            ]}
            key={Math.random()}
        />
    );
};
