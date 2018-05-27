package server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAutenticationSecurityConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new AuthChannelInterceptorAdapter(this.webSocketAuthenticatorService));
    }
}
