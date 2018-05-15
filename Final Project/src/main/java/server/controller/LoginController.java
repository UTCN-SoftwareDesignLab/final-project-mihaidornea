/*package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import server.dto.UserDto;
import server.dto.builder.UserDtoBuilder;
import server.messages.LoginMessage;
import server.messages.RegisterMessage;
import server.messages.ServerResponseMessage;
import server.service.user.UserService;

@Controller
public class LoginController {


    @Autowired
    private UserService userService;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public LoginController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/login/Login")
    @SendTo("/loginBroker/login")
    public ServerResponseMessage login(Message<LoginMessage> loginMessage) throws Exception {
        LoginMessage message = loginMessage.getPayload();
        UserDto userDto = userService.findByUsername(message.getUsername());
        if (userDto == null) {
            return new ServerResponseMessage("Incorrect username!");
        } else if (!userDto.getPassword().equals(message.getPassword())){
            return new ServerResponseMessage("Incorrect Password!");
        } else {
            return new ServerResponseMessage("Access Granted!");
        }
    }

    @MessageMapping("/register/Registration")
    @SendTo("/registerBroker/registration")
    public ServerResponseMessage register(Message<RegisterMessage> registerMessage) throws Exception{
        System.out.println("yay");
        RegisterMessage message = registerMessage.getPayload();
        UserDto userDto = new UserDtoBuilder()
                .setFirstName(message.getFirstName())
                .setLastName(message.getLastName())
                .setUsername(message.getUsername())
                .setPassword(message.getPassword())
                .setLatitude(Double.parseDouble(message.getLatitude()))
                .setLongitude(Double.parseDouble(message.getLongitude()))
                .build();

        userService.create(userDto);
        return new ServerResponseMessage("User Successfully Created!");
    }

    @GetMapping("/login")
    public String loginForm(){
        return "/login";
    }

    @GetMapping("/register")
    public String registerForm(){
        return "/register";
    }

}
*/