import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import { FeedPage } from './pages/FeedPage';
import { ProfilePage } from './pages/ProfilePage';
import { CommunityPage } from './pages/CommunityPage';
import { CommunityPageMode } from './components/community/CommunityPageMode';
import LoginPage from './pages/LoginPage';
import { RegistrationPage } from './pages/RegistrationPage';
import { EditProfilePage } from './pages/EditProfilePage';
import { ChatPage } from './pages/ChatPage';
import { ErrorPage } from './pages/ErrorPage';

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
                <Route exact path="/community">
                    <CommunityPage
                        modes={[
                            CommunityPageMode.FRIENDS,
                            CommunityPageMode.SUBSCRIBERS,
                            CommunityPageMode.SUBSCRIPTIONS,
                            CommunityPageMode.COMMUNITIES,
                        ]}
                    />
                </Route>
                <Route exact path="/users">
                    <CommunityPage modes={[CommunityPageMode.ALL]} />
                </Route>
                <Route exact path="/editProfile">
                    <EditProfilePage />
                </Route>
                <Route exact path="/chat/:id">
                    <ChatPage />
                </Route>
                <Route path="/error/:num">
                    <ErrorPage />
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
            <Route path="/error/:num">
                <ErrorPage />
            </Route>
            <Redirect to="/login" />
        </Switch>
    );
};
