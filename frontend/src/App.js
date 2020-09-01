import React, { useEffect, useState, useCallback } from 'react';
import { useRoutes } from './routes';
import { BrowserRouter } from 'react-router-dom';
import { useAuth } from '../src/hooks/auth.hook';
import { AuthContext } from './context/AuthContext';
import { AuthorizedNavBar } from './components/AuthorizedNavBar';
import { NotAuthorizedNavBar } from './components/NotAuthorizedNavBar';
import { NewsContext, UserContext } from './dev/DevContext';
import { TOKEN_NAME } from './constants/constants';

const publications = [
    {
        theme: 'Some theme',
        text: 'Sample text just to see how far does it goes',
        date: new Date().getDay() + ':' + new Date().getMonth(),
        time: new Date().getHours() + ':' + new Date().getMinutes(),
        author: {
            id: 1,
            firstName: 'Max',
            lastName: 'Chatsky',
        },
        authorAvatar: null,
        images: null,
    },
    {
        theme: 'Some another theme',
        text:
            'Sample text just to see how far does it goes. Sample text just to see how far does it goes. Sample text just to see how far does it goes',
        date: new Date().getDay() + ':' + new Date().getMonth(),
        time: new Date().getHours() + ':' + new Date().getMinutes(),
        author: {
            id: 1,
            firstName: 'Max',
            lastName: 'Chatsky',
        },
        authorAvatar: null,
        images: null,
    },
];

const user = {
    id: 1,
    username: 'Chatsky',
    firstName: 'Максим',
    lastName: 'Федько',
    sex: 'MALE',
};

export default () => {
    const { isToken, login, logout } = useAuth();

    const authorized = isToken();
    const routes = useRoutes(authorized);

    return (
        <div>
            <UserContext.Provider value={user}>
                <NewsContext.Provider value={publications}>
                    <AuthContext.Provider value={{ login, logout }}>
                        <BrowserRouter>
                            {authorized ? <AuthorizedNavBar /> : <NotAuthorizedNavBar />}

                            {routes && <div className="page-body">{routes}</div>}
                        </BrowserRouter>
                    </AuthContext.Provider>
                </NewsContext.Provider>
            </UserContext.Provider>
        </div>
    );
};
