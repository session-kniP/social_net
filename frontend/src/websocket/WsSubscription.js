import React, { useState } from 'react';
import { REMOTE, TOKEN_NAME } from '../constants/constants';
import { WS_SERVER } from '../constants/mappings';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import 'regenerator-runtime/runtime';

export class WsSubscription {

    constructor(subscription, messageCallback) {
        this.subscription = subscription;
        this.messageCallback = messageCallback;
        this.client = null;
    }
    //props: subscription, messageCallback

    connect(onConnected, onError) {
        const token = localStorage.getItem(TOKEN_NAME);

        //WS_SERVER = '/ws'
        const socket = new SockJS(REMOTE + WS_SERVER);

        //deprecated
        this.client = Stomp.over(socket);
        this.client.debug = () => {};

        this.client.connect(
            {
                Authorization: 'Bearer_' + token,
            },
            () => onConnected(this.client),
            () => onError()
        );

        return this.client;
    };

    getClient() {
        return this.client;
    }

};
