import React from 'react';
import NavBar from './NavBar';

export const NotAuthorizedNavBar = () => {
    return(
        <NavBar
            // navClassName='not-authorized'
            authLinks={[{ title: 'Log In', href: '/login' }, { title: 'Register', href: '/registration' }]}
            key={Math.random()}
        />
    );
};
