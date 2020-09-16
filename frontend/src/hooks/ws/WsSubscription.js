import React, { useState } from 'react';
import { REMOTE, TOKEN_NAME } from '../../constants/constants';
import { WS_SERVER } from '../../constants/mappings';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import 'regenerator-runtime/runtime';

export class WsSubscription extends React.Component {
    //props: subscription, messageCallback
    constructor(props) {
        super(props);

        this.onConnected = this.onConnected.bind(this);
        this.connect = this.connect.bind(this);
        this.onError = this.onError.bind(this);
        this.onMessageReceived = this.onMessageReceived.bind(this);
        this.getClient = this.getClient.bind(this);

        this.state = {
            client: undefined,
            subscription: props.subscription,
            messageCallback: props.messageCallback,
        };
    }

    connect(onConnected, onError) {
        //WS_SERVER = '/ws'
        const token = localStorage.getItem(TOKEN_NAME);

        const socket = new SockJS(REMOTE + WS_SERVER);

        //deprecated
        const client = Stomp.over(socket);
        client.debug = () => {};

        client.connect(
            {
                Authorization: 'Bearer_' + token,
            },
            () => onConnected(client),
            () => onError()
        );
    }
}
