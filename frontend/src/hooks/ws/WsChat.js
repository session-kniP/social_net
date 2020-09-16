import {} from '../../constants/mappings';
import PropTypes from 'prop-types';
import { WsSubscription } from './WsSubscription';
import { WS_CHAT, WS_CHAT_SUBSCRIPTION } from '../../constants/mappings';
import 'regenerator-runtime/runtime';

class WsChat extends WsSubscription {
    //props: chatId
    constructor(props) {
        
        super({ subscription: WS_CHAT_SUBSCRIPTION + '/' + props.chatId });

        this.sendMessage = this.sendMessage.bind(this);
        this.messageCallback = this.messageCallback.bind(this);
        this.onConnected = this.onConnected.bind(this);
        this.onDisconnected = this.onDisconnected.bind(this);

        const newState = this.state;
        newState.chatId = props.chatId;
        newState.chatCallback = props.chatCallback;

        this.state = newState;        
    }

    messageCallback(body) {
        console.log('MESSAGE CALLBACK', body);
    }

    connect() {
        super.connect(this.onConnected, this.onError);
    }

    onConnected(client) {
        client.subscribe(this.state.subscription, this.onMessageReceived);

        const newState = this.state;
        newState.client = client;

        // this.setState(newState);
        this.state = newState;
        // stateCallback(newState);
        console.log('ON CONNECTED');
    }

    onDisconnected(client) {
        
    }

    onMessageReceived(payload) {
        this.state.chatCallback(JSON.parse(payload.body));
    }

    onError() {}

    getClient() {
        return this.state.client;
    }

    sendMessage(message) {
        try {
            this.getClient().send(
                WS_CHAT + '/' + this.state.chatId + '/sendMessage',
                {},
                JSON.stringify({ text: message })
            );
        } catch (e) {
            console.error(e);
        }

        // super.state.client.send(WS_CHAT + '/' + this.props.chatId, {}, JSON.stringify(message));
    }

    loadChat({ page, howMuch }) {
        page = page || 0;
        howMuch = howMuch || 20;

        this.getClient().send(WS_CHAT + '/' + this.state.chatId + '/getChat');
    }
}

WsChat.propTypes = {
    message: PropTypes.object,
};

export default WsChat;
