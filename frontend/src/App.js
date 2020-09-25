import React, { useEffect, useState, useCallback } from 'react';
import { useRoutes } from './routes';
import { BrowserRouter } from 'react-router-dom';
import { useAuth } from '../src/hooks/auth.hook';
import { AuthContext } from './context/AuthContext';
import { AuthorizedNavBar } from './components/AuthorizedNavBar';
import { NotAuthorizedNavBar } from './components/NotAuthorizedNavBar';
import { NewsContext, UserContext } from './dev/DevContext';
import { TOKEN_NAME } from './constants/constants';

export default () => {
    const { isToken, login, logout } = useAuth();

    const authorized = isToken();
    const routes = useRoutes(authorized);

    return (
        <div className="h-100">
            {/* <UserContext.Provider value={user}> */}
            {/* <NewsContext.Provider value={publications}> */}
            <AuthContext.Provider value={{ login, logout }}>
                <BrowserRouter>
                    {authorized ? (
                        <AuthorizedNavBar />
                    ) : (
                        <NotAuthorizedNavBar />
                    )}
                    <div className="container-fluid w-100 p-1 h-100">
                        <div className="row justify-content-sm-center flex-wrap w-100 ml-0 h-100">
                            <div className="col-sm-12 col-md-6 col-lg-5 w-100 px-0 pb-0 h-100">
                                {routes && (
                                    <div className="page-body">{routes}</div>
                                )}
                            </div>
                        </div>
                    </div>
                </BrowserRouter>
            </AuthContext.Provider>
            {/* </NewsContext.Provider> */}
            {/* </UserContext.Provider> */}
        </div>
    );
};
