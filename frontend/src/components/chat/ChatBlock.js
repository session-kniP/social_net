import React, { useEffect } from 'react';
import { ChatMessage } from './ChatMessage';
import { USER_ID } from '../../constants/constants';

const SCROLL_DECLINE_OFFSET = 100;

export const ChatBlock = React.forwardRef((props, ref) => {
    const chat = props.chat;
    const userId = localStorage.getItem(USER_ID);
    let lastDate;
    const lastMessageRef = React.createRef();

    const isNewDate = (date) => {
        if (!lastDate || lastDate != date) {
            lastDate = date;
            return true;
        }

        return false;
    };

    const scroll = (element) => {
        if (element) {
            element.scrollIntoView();
        }
    };

    useEffect(() => {
        const trueScroll = ref.current.scrollTop + ref.current.offsetHeight;
        if (
            trueScroll > ref.current.scrollHeight - SCROLL_DECLINE_OFFSET ||
            ref.current.scrollTop === 0
        ) {
            //todo fix crutch with === 0
            ref.current.scrollTop = ref.current.scrollHeight;
            scroll(lastMessageRef.current);
        }
    });

    return (
        <div
            className="container chat-block"
            aria-live="polite"
            aria-relevant="additions"
        >
            {chat.title && <div>{chat.title}</div>}

            {chat.chatList.length > 0 ? (
                chat.chatList
                    .slice(0)
                    .reverse()
                    .map((msg, index) => {
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
                                    isOwner={
                                        msg.sender.id == userId ? true : false
                                    }
                                    newDate={isNewDate(msg.date)}
                                    ref={
                                        index == chat.chatList.length - 1
                                            ? lastMessageRef
                                            : null
                                    }
                                />
                            </React.Fragment>
                        );
                    })
            ) : (
                <div className="mx-auto col-4 text-center">No messages yet</div>
            )}
        </div>
    );
});
