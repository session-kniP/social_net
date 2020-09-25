import React, { useState, useEffect, useCallback } from 'react';
import { WsChat } from '../hooks/ws/wsChat.hook';
import { useParams } from 'react-router-dom';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { ChatForm } from '../components/form/ChatFrom';

import '../styles/index.css';
import '../styles/chat.css';
import '../styles/publicationForm.css';
import { ChatBlock } from '../components/chat/ChatBlock';
import { DataSource } from '../data/DataSource';

const useForceUpdate = () => {
    const [value, setValue] = useState(0); // integer state
    return () => setValue((value) => ++value); // update the state to force render
};

export const ChatPage = () => {
    const id = useParams().id;
    const { httpRequest } = useHttpRequest();

    const source = new DataSource(httpRequest);
    const forceUpdate = useForceUpdate();
    // const wsChat = new WsChat(id, chatCallback, source);

    const [wsChat, setWsChat] = useState(null);
    const [chat, setChat] = useState(null);

    let textInput = React.createRef();

    const loadElements = async () => {
        const chatCallback = (chatInstance) => {
            chat.chatList = [chatInstance].concat(chat.chatList);
            setChat(chat);
            forceUpdate();
        };

        const wsChat = new WsChat(id, chatCallback, source);
        const chat = await wsChat.loadChat();
        setChat({ title: chat.title, chatList: chat.chatList });
        setWsChat(wsChat);

        wsChat.connectChat();
    };

    useEffect(() => {
        loadElements();
    }, []);

    const sendBtnHandler = (event) => {
        const message = textInput.current.value;
        wsChat.sendMessage(message);
        textInput.current.value = '';
        textInput.current.focus();
    };

    return (
        <div className="content-block-main justify-items-center">
            {chat && chat.chatList ? (
                <div className="chat row align-items-end mb-2">
                    <ChatBlock chat={chat} />
                </div>
            ) : (
                <div>No messages at all</div>
            )}
            <div className="chat-form col-12 p-0 py-2">
                <ChatForm sendBtnHandler={sendBtnHandler} ref={textInput} />
            </div>
        </div>
    );
};
