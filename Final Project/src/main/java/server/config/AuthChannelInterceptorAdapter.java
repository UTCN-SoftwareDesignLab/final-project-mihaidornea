package server.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class AuthChannelInterceptorAdapter extends ChannelInterceptorAdapter {

    private static final String USERNAME_HEADER = "USERNAME_HEADER";
    private static final String PASSWORD_HEADER = "PASSWORD_HEADER";
    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    public AuthChannelInterceptorAdapter(WebSocketAuthenticatorService webSocketAuthenticatorService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.SUBSCRIBE == accessor.getCommand()){
            String topic = accessor.getDestination().substring(0, 9);
            if(!topic.equals("/register")) {
                final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
                final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);

                final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAutheticatedOrFail(username, password);
                accessor.setUser(user);
            }
        }
        return message;
    }
}
