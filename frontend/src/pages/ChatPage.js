import React, { useState, useEffect, useCallback } from 'react';
import WsChat from '../hooks/ws/WsChat';
import {useParams} from 'react-router-dom'
import { TextInput } from '../components/TextInput';
import {useHttpRequest} from '../api/request/httpRequest.hook';
import {COMMUNITY_CHAT} from '../constants/mappings';
import {ChatForm} from '../components/form/ChatFrom';

import '../styles/index.css';
import '../styles/chat.css';
import '../styles/publicationForm.css';

export const ChatPage = () => {
    const [messages, setMessages] = useState(null);
    const {httpRequest} = useHttpRequest();
    let textInput = React.createRef();

    const id = useParams().id;
    let chat;

    const chatCallback = (chatInstance) => {
        console.log(chatInstance);
    }

    const loadChat = useCallback(async () => {
        try {
            const response = await httpRequest({url: '/'})
        } catch (e) {

        }
    }, [])

    useEffect(() => {
        chat = new WsChat({ chatId: id, chatCallback: chatCallback });
        chat.connect();


    })

    const sendBtnHandler = (event) => {
        const message = textInput.current.value;
        chat.sendMessage(message);
        textInput.current.value = '';
    }

    return (
        <div className="content-block-main">
            <div className="chat-from">
                <ChatForm sendBtnHandler={sendBtnHandler} ref={textInput}/>
                
            </div>
        </div>
    )

};
