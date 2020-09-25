package com.sessionknip.socialnet.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/chat/**", "/event/**").permitAll()
                .simpSubscribeDestMatchers("/chatBroker/**", "/eventBroker/**").permitAll()
                .simpTypeMatchers(
                        SimpMessageType.SUBSCRIBE,
                        SimpMessageType.CONNECT,
                        SimpMessageType.DISCONNECT,
                        SimpMessageType.CONNECT_ACK,
                        SimpMessageType.MESSAGE).permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }




}
