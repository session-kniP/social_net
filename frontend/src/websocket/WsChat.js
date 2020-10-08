import React from 'react';
import { WS_CHAT, WS_CHAT_SUBSCRIPTION } from '../constants/mappings';
import 'regenerator-runtime/runtime';
import { WsSubscription } from './WsSubscription';
import { COMMUNITY_CHAT } from '../constants/mappings';
import { ChatType } from '../components/chat/ChatType';
import { ChainException } from '../exception/ChainException';

export class WsChat extends React.Component {
    constructor(chatType, chatCallback, source) {
        super();

        this.onConnected = this.onConnected.bind(this);
        this.connectChat = this.connectChat.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
        this.onMessageReceived = this.onMessageReceived.bind(this);

        this.state = {
            client: null,
            chatId: null,
            chatType: chatType,
            chatCallback: chatCallback,
            source: source,
            subscription: null,
            wsSubscription: null,
        };
    }

    messageCallback(body) {
        console.log('MESSAGE CALLBACK', body);
    }

    connectChat(id) {
        const subscription = WS_CHAT_SUBSCRIPTION + '/' + id;
        const wsSubscription = new WsSubscription(subscription);

        wsSubscription.connect(this.onConnected, this.onError);

        const newState = this.state;
        newState.chatId = id;
        newState.subscription = subscription;
        newState.wsSubscription = wsSubscription;

        this.setState(newState);
    }

    onConnected(client) {
        try {
            client.subscribe(this.state.subscription, this.onMessageReceived);
            const newState = this.state;
            newState.client = client;
            this.setState(newState);
        } catch (e) {
            console.error(e);
        }
    }

    onDisconnected(client) {}

    onMessageReceived(payload) {
        this.state.chatCallback(JSON.parse(payload.body));
        // return payload;
    }

    onError() {}

    sendMessage(message) {
        try {
            this.state.client.send(
                `${WS_CHAT}/${this.state.chatId}/sendMessage`,
                {},
                JSON.stringify({ text: message })
            );
        } catch (e) {
            console.error(e);
        }

        // super.state.client.send(WS_CHAT + '/' + this.props.chatId, {}, JSON.stringify(message));
    }

    async getChatId(interlocutorId) {
        try {
            const chat = await this.state.source.call({
                url: `${COMMUNITY_CHAT}/getPrivateId?interlocutorId=${interlocutorId}`,
                method: 'GET',
            });

            return chat;
        } catch (e) {
            throw new ChainException({message: "Can't get chat id", cause: e});
        }
    }

    async loadChat(id, page, howMuch) {
        page = page || 0;
        howMuch = howMuch || 20;

        try {
            const chat = await this.state.source.call({
                url: `${COMMUNITY_CHAT}/${id}?page=${page}&howMuch=${howMuch}`,
                method: 'GET',
            });

            console.log(chat);
            return chat;
        } catch (e) {
            //todo Write message to html
            console.log(e.message);
        }
    }
}
