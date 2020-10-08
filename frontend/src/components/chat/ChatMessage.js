import React from 'react';

export const ChatMessage = React.forwardRef((props, ref) => {
    const message = props.message;
    const isOwner = props.isOwner;

    const firstName = message.sender.userInfo.firstName;
    const lastName = message.sender.userInfo.lastName;

    const username = message.sender.username;

    return (
        <div className="container align-self-end my-1" ref={ref}>
            <div
                className={`row ${
                    isOwner ? 'justify-content-start' : 'justify-content-end'
                }`}
            >
                <div
                    className={`col-8 col-md-4 ${
                        isOwner ? 'text-left' : 'text-right'
                    }`}
                >
                    {firstName && lastName
                        ? `${firstName} ${lastName}`
                        : { username }}
                </div>
            </div>
            <div
                className={`row ${
                    isOwner ? 'justify-content-start' : 'justify-content-end'
                }`}
            >
                <div
                    className={
                        'col-auto chat-message border rounded-lg ' +
                        (isOwner ? 'bg-info' : 'bg-secondary')
                    }
                >
                    <div className="row">
                        <div className="col-auto" style={{ lineHeight: 1.1 }}>
                            <span>{message.text}</span>
                        </div>
                        <div className="col-1"></div>
                    </div>

                    {/* <div className="row">&nbsp;</div> */}
                    <div className="row justify-content-right">
                        {/* <div className="col-11-auto"></div> */}
                        <div className="col datetime text-right mr-0">
                            {message.time}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
});
