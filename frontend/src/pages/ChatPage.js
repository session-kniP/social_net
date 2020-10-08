import React, { useState, useEffect } from 'react';
import { WsChat } from '../websocket/WsChat';
import { useParams, useLocation } from 'react-router-dom';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { ChatForm } from '../components/form/ChatFrom';
import { ChatBlock } from '../components/chat/ChatBlock';
import { DataSource } from '../data/DataSource';
import { isMobile } from 'mobile-device-detect';
import { ChatType } from '../components/chat/ChatType';

import '../styles/index.scss';
import '../styles/chat.scss';
import '../styles/publicationForm.scss';

//crutch
const useForceUpdate = () => {
    const [value, setValue] = useState(true); // integer state
    return () => setValue((value) => !value); // update the state to force render
};

export const ChatPage = (props) => {
    let id = useParams().id;
    const location = useLocation();
    const type = location.state ? location.state.type : ChatType.DIALOGUE;
    const { httpRequest } = useHttpRequest();

    const source = new DataSource(httpRequest);
    const forceUpdate = useForceUpdate();
    // const wsChat = new WsChat(id, chatCallback, source);

    const [wsChat, setWsChat] = useState(null);
    const [chat, setChat] = useState(null);

    const textInput = React.createRef();
    const chatBlock = React.createRef(); //actually reference on chat, not chat-block!

    const loadElements = async () => {
        try {
            const chatCallback = (chatInstance) => {
                chat.chatList = [chatInstance].concat(chat.chatList);
                setChat(chat);
                forceUpdate();
            };

            const wsChat = new WsChat(type, chatCallback, source);

            if (type == ChatType.PRIVATE) {
                await wsChat.getChatId(id).then((result) => (id = result));
            }

            const chat = await wsChat.loadChat(id);

            setChat({ title: chat.title, chatList: chat.chatList });
            setWsChat(wsChat);

            wsChat.connectChat(id);
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => {
        formUpdatedHandler();
        loadElements();
    }, []);

    const sendBtnHandler = (event) => {
        const message = textInput.current.value;
        wsChat.sendMessage(message);
        textInput.current.value = '';
        textInput.current.focus();
    };

    const percent = (fullValue, percent) => {
        return (percent * fullValue) / 100;
    };

    const formUpdatedHandler = () => {
        const el = chatBlock.current;

        if (el) {
            const fullHeight = isMobile
                ? screen.availHeight
                : document.body.clientHeight;
            // document.body.clientHeight;
            const textHeight = textInput.current.clientHeight;

            setTimeout(() => {
                el.style.height = `auto`;
                el.style.height = `${
                    fullHeight -
                    percent(fullHeight, isMobile ? 36 : 14.7) -
                    textHeight
                }px`; //todo think how to remove these magical numbers
            }, 0);
        }
    };

    return (
        <div className="content-block-main justify-items-center">
            {chat && chat.chatList && (
                <div
                    ref={chatBlock}
                    className="chat col-12 col-sm-11 m-auto row align-items-end mb-2"
                >
                    <ChatBlock ref={chatBlock} chat={chat} />
                </div>
            )}
            <div className="chat-form col-12 col-sm-11 m-auto p-0 py-2">
                <ChatForm
                    sendBtnHandler={sendBtnHandler}
                    formCallback={formUpdatedHandler}
                    ref={textInput}
                />
            </div>
        </div>
    );
};
