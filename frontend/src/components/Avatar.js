import React from 'react';
import '../styles/profile.scss';

const Avatar = ({ onModalShow, src }) => {
    return (
        <a className="imageLink" onClick={onModalShow}>
            <div
                className="avatar"
                style={{ backgroundImage: `url(${src})`}}
            >
            </div>
        </a>
    );
};

export default Avatar;
