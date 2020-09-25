import React from 'react';
import { WS_CHAT, WS_CHAT_SUBSCRIPTION } from '../../constants/mappings';
import 'regenerator-runtime/runtime';
import { WsSubscription } from './wsSubscription.hook';
import { COMMUNITY_CHAT } from '../../constants/mappings';
import { useHttpRequest } from '../../api/request/httpRequest.hook';

export class WsChat extends React.Component {
    constructor(chatId, chatCallback, source) {

        super();
        const subscription = WS_CHAT_SUBSCRIPTION + '/' + chatId;
        
        this.onConnected = this.onConnected.bind(this);
        this.connectChat = this.connectChat.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
        this.onMessageReceived = this.onMessageReceived.bind(this);

        this.state = {
            client: null,
            chatId: chatId,
            chatCallback: chatCallback,
            source: source,
            subscription: subscription,
            wsSubscription: new WsSubscription(subscription)
        }


    }
    //props: chatId

    messageCallback(body) {
        console.log('MESSAGE CALLBACK', body);
    }

    connectChat() {
        this.state.wsSubscription.connect(this.onConnected, this.onError);
    }

    onConnected(client) {
        try {
            client.subscribe(this.state.subscription, this.onMessageReceived);
            const newState = this.state;
            newState.client = client;
            this.setState(newState);
            // this.client = client;
            // this.setState(newState);
            // stateCallback(newState);
        } catch(e) {
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
                WS_CHAT + '/' + this.state.chatId + '/sendMessage',
                {},
                JSON.stringify({ text: message })
            );
        } catch (e) {
            console.error(e);
        }

        // super.state.client.send(WS_CHAT + '/' + this.props.chatId, {}, JSON.stringify(message));
    }

    async loadChat(page, howMuch) {
        page = page || 0;
        howMuch = howMuch || 20;

        try {
            const chat = await this.state.source.call({
                url: `${COMMUNITY_CHAT}/${this.state.chatId}?page=${page}&howMuch=${howMuch}`,
                method: 'GET',
            });

            return chat;
        } catch (e) {
            //todo Write message to html
            console.log(e.message);
        }
    }
}
