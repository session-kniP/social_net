package com.sessionknip.socialnet.web.config;

import com.sessionknip.socialnet.web.security.TokenProvider;
import com.sessionknip.socialnet.web.security.TokenProviderException;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.security.WebSocketPrincipal;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final TokenProvider provider;

    private final UserDetailsService userDetailsService;

    @Value("${jwt.token.header}")
    private String jwtHeader;

    public WebSocketConfig(TokenProvider provider, UserDetailsService userDetailsService) {
        this.provider = provider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // requests from users
        registry.setApplicationDestinationPrefixes("/chat", "/event");
        // subscription responses
        registry.enableSimpleBroker("/chatBroker", "/eventBroker");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new ChannelInterceptorAdapter() {
            @SneakyThrows
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                assert accessor != null;
                String token = provider.resolveToken(accessor.getFirstNativeHeader(jwtHeader));

                try {
                    if (token != null && provider.validateToken(token)) {
                        String username = provider.getUsername(token);
                        WebSocketPrincipal principal = new WebSocketPrincipal((UserDetailsImpl) userDetailsService.loadUserByUsername(username));

                        accessor.setUser(principal);
                    }
                } catch (TokenProviderException e) {
                    throw new WebSocketException("Token was null or invalid", e);
                }

                return message;
            }
        });
    }


}
