package server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import server.dto.UserDto;
import server.service.user.UserService;

import java.util.Collections;

@Component
public class WebSocketAuthenticatorService {

    @Autowired
    private UserService userService;
    BCryptPasswordEncoder encoder;

    public UsernamePasswordAuthenticationToken getAutheticatedOrFail(final String username, final String password){
        encoder = new BCryptPasswordEncoder();
        UserDto userDto = userService.findByUsername(username);
        String encodedPassword = encoder.encode(password);
        if(userDto==null){
            throw new BadCredentialsException("Incorect username!");
        } else if (!encoder.matches(password, userDto.getPassword())){
            throw new BadCredentialsException("Invalid Password!");
        } else {
            return new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton((GrantedAuthority)()->"USER"));
        }
    }
}
