import React from 'react';
import '../styles/publication.scss';

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
                    <label className="datetime">{props.date + ' | ' + props.time}</label>
                </div>
            </div>
            <div className="publication-content">
                <label className="publication-theme m-0 p-1">{props.theme}</label><br/>
                <label className="publication-text m-0" contentEditable={false}  >{props.text}</label>
            </div>
        </div>
    );
};
