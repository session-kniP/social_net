import React, { useEffect } from 'react';
import { ChatMessage } from './ChatMessage';
import { USER_ID } from '../../constants/constants';

export const ChatBlock = (props) => {
    const chat = props.chat;
    const userId = localStorage.getItem(USER_ID);
    let lastDate;
    let lastRef = React.createRef();

    const isNewDate = (date) => {
        if (!lastDate || lastDate != date) {
            lastDate = date;
            return true;
        }

        return false;
    };

    useEffect(() => {
        lastRef.current.scrollIntoView();
    })

    return (
        <div className="container chat-block pb-2">
            {chat.title && <div>{chat.title}</div>}

            {chat.chatList ? (
                chat.chatList.slice(0).reverse().map((msg, index) => {
                    return (
                        <React.Fragment key={index}>
                            {isNewDate(msg.date) && (
                                <div className="row border-top border-bottom">
                                    <div className="col-12 text-center">
                                        {msg.date}
                                    </div>
                                </div>
                            )}
                            <ChatMessage
                                message={msg}
                                isOwner={msg.sender.id == userId ? true : false}
                                newDate={isNewDate(msg.date)}
                                ref={(index == chat.chatList.length - 1) ? lastRef : null}
                            />
                        </React.Fragment>
                    );
                })
            ) : (
                <div>No messages</div>
            )}
        </div>
    );
};
