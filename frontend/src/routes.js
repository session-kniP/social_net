import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import { FeedPage } from './pages/FeedPage';
import { ProfilePage } from './pages/ProfilePage';
import { HomePage } from './pages/HomePage';
import { LoginPage } from './pages/LoginPage';
import { RegistrationPage } from './pages/RegistrationPage';
import { EditProfilePage } from './pages/EditProfilePage';

export const useRoutes = (isAuthorized) => {
    if (isAuthorized) {
        return (
            <Switch>
                <Route exact path="/user">
                    <ProfilePage />
                </Route>
                <Route exact path="/feed">
                    <FeedPage />
                </Route>
                <Route exact path="/editProfile">
                    <EditProfilePage />
                </Route>
                <Route exact path="/">
                    <HomePage />
                </Route>
                <Redirect to="/" />
            </Switch>
        );
    }

    return (
        <Switch>
            <Route exact path="/login">
                <LoginPage />
            </Route>
            <Route exact path="/registration">
                <RegistrationPage />
            </Route>
            <Route exact path="/">
                <HomePage />
            </Route>
            <Redirect to="/" />
        </Switch>
    );
};
