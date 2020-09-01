import React from 'react';
import '../styles/publication.css';

//{ theme, text, date, time, author, authorAvatar, images }
export const Publication = ({ props }) => {
    return (
        <div className="publication">
            <div className="publication-info">
                <div className="author-avatar-wrapper">
                    <img className="author-avatar-tn"></img>
                </div>
                <div className="publication-author-date-info">
                    <a className="publication-author-info" href={`/profile/${props.author.id}`}>
                        {props.author.userInfo.firstName + ' ' + props.author.userInfo.lastName}
                    </a><br/>
                    <label className="publication-date-info">{props.date + ' | ' + props.time}</label>
                </div>
            </div>
            <div className="publication-content">
                <label className="publication-theme">{props.theme}</label><br/>
                <label className="publication-text" contentEditable={false}  >{props.text}</label>
            </div>
        </div>
    );
};
