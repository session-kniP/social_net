import React from 'react';
import '../styles/profile.css';

const Avatar = ({onModalShow, src}) => {
    return (
        <a className="imageLink" onClick={onModalShow}>
            <img
                tabIndex={0}
                className="avatar"
                src={src}
                onErrorCapture={(e) => (e.target.src = '../../resources/images/default.jpg')}
            />
            <br />
        </a>
    );
};

export default Avatar;
