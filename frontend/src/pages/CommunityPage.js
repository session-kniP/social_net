import React from 'react';
import '../styles/index.scss';
import '../styles/community.scss';
import '../styles/profile.scss';
import { CommunitySwitch } from '../components/community/CommunitySwitch';


export const CommunityPage = ({ modes }) => {
    return <CommunitySwitch modes={modes}/>
};
