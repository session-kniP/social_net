import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import { FeedPage } from './pages/FeedPage';
import { ProfilePage } from './pages/ProfilePage';
import { CommunityPage, CommunityPageMode } from './pages/CommunityPage';
import LoginPage from './pages/LoginPage';
import { RegistrationPage } from './pages/RegistrationPage';
import { EditProfilePage } from './pages/EditProfilePage';
import { ChatPage } from './pages/ChatPage';

export const useRoutes = (isAuthorized) => {
    if (isAuthorized) {
        return (
            <Switch>
                <Route exact path="/profile/:id">
                    <ProfilePage />
                </Route>
                <Route exact path="/profile">
                    <ProfilePage />
                </Route>
                <Route exact path="/feed">
                    <FeedPage />
                </Route>
                <Route exact path="/subscriptions">
                    <CommunityPage mode={CommunityPageMode.SUBSCRIPTIONS} />
                </Route>
                <Route exact path="/subscribers">
                    <CommunityPage mode={CommunityPageMode.SUBSCRIBERS} />
                </Route>
                <Route exact path="/friends">
                    <CommunityPage mode={CommunityPageMode.FRIENDS} />
                </Route>
                <Route exact path="/users">
                    <CommunityPage mode={CommunityPageMode.ALL} />
                </Route>
                <Route exact path="/editProfile">
                    <EditProfilePage />
                </Route>
                <Route exact path="/chat/:id">
                    <ChatPage />
                </Route>
                <Redirect to="/profile" />
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
            {/* <Route exact path="/">
                <HomePage />
            </Route> */}
            <Redirect to="/login" />
        </Switch>
    );
};
