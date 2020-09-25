import React from 'react';
import '../styles/profile.css';

const Avatar = ({ onModalShow, src }) => {
    console.log(src);
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
