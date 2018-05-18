package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import server.dto.MessageDto;
import server.dto.UserDto;
import server.dto.builder.UserDtoBuilder;
import server.messages.Content;
import server.messages.GeneralMessage;
import server.messages.IMessage;
import server.messages.ServerResponseMessage;
import server.service.message.MessageService;
import server.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GeneralController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public GeneralController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat/{topic}")
    @SendTo("/broker/messages")
    public ServerResponseMessage process(@DestinationVariable String topic, Message<GeneralMessage> message) throws Exception {
        /*String response = "";
        switch (topic){
            case "Register":
                response = register(message.getPayload().getContent());
                break;
            case "Update":
                response = update(message.getPayload().getContent());
                break;
            case "Send":
                response = message.getPayload().getContent();
            default:
                break;
        }
        return new ServerResponseMessage(response);
        */
        return new ServerResponseMessage("NOT YET");
    }

    @MessageMapping("/login/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage login(@DestinationVariable String username, Message<GeneralMessage> message1) throws Exception{
        Content content = message1.getPayload().getContent();
        try {
            UserDto userDto = userService.findByUsername(content.getUsername());
            if (userDto == null) {
                return new ServerResponseMessage("Incorrect username!");
            } else if (!userDto.getPassword().equals(content.getPassword())){
                return new ServerResponseMessage("Incorrect Password!");
            } else {
                return new ServerResponseMessage("Access Granted!");
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
        return new ServerResponseMessage("Invalid username");
    }

    @MessageMapping("/im/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage sendIm(@DestinationVariable String username, Message<GeneralMessage> message) throws Exception{
        String response = "";
        try {
            Content content = message.getPayload().getContent();
            response = content.getMessage();
            String fromUser = content.getUsername();
            UserDto fromUserDto = userService.findByUsername(fromUser);
            UserDto toUserDto = userService.findByUsername(username);
            System.out.println(fromUserDto.getUsername());
            System.out.println(toUserDto.getUsername());
            messageService.create(fromUserDto, toUserDto, content.getMessage());
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return new ServerResponseMessage(response);
    }

    @MessageMapping("/register/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage register(@DestinationVariable String username, Message<GeneralMessage> message) throws Exception{
        Content content = message.getPayload().getContent();
        try{
            UserDto userDto = new UserDtoBuilder()
                    .setFirstName(content.getFirstName())
                    .setLastName(content.getLastName())
                    .setUsername(content.getUsername())
                    .setPassword(content.getPassword())
                    .setLatitude(content.getLatitude())
                    .setLongitude(content.getLongitude())
                    .build();
            if (userService.create(userDto))
                return new ServerResponseMessage("User Successfully Created!");
            else return new ServerResponseMessage("Error when creating the user!");
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
            return new ServerResponseMessage("Error when creating the user!");
    }

    @MessageMapping("/update/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage update(@DestinationVariable String username, Message<GeneralMessage> message){
        Content content = message.getPayload().getContent();
        UserDto userDto = userService.findByUsername(content.getUsername());
        if (userDto != null) {
            if(userService.update(userDto, content.getLatitude(), content.getLongitude()))
                return new ServerResponseMessage("Update Successfully!");
            else return new ServerResponseMessage("Failed to Update!");
        } else return new ServerResponseMessage("Invalid username!");
    }

    @MessageMapping("/getIM/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage getMessages(@DestinationVariable String username, Message<GeneralMessage> message){
        Content content = message.getPayload().getContent();
        UserDto toUserDto = userService.findByUsername(content.getUsername());
        UserDto fromUserDto = userService.findByUsername(username);
        List<MessageDto> messages = messageService.findByFromUserAndToUser(fromUserDto, toUserDto);
        ArrayList<IMessage> iMessages = new ArrayList<>();
        for (MessageDto messageDto : messages){
            iMessages.add(new IMessage(messageDto.getFromUserDto().getUsername(), messageDto.getToUserDot().getUsername(), messageDto.getContent()));
        }
        return new ServerResponseMessage("Got'em!", iMessages);
    }

    @MessageMapping("/getUsers/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage getUsers(@DestinationVariable String username, Message<GeneralMessage> message){
        UserDto userDto = userService.findByUsername(username);
        double latitude = userDto.getLatitude();
        double longitude = userDto.getLongitude();
        List<UserDto> users = userService.findByLatitudeBetweenAndLongitudeBetween(latitude - 0.05,
                                                                        latitude + 0.05,
                                                                        longitude - 0.05,
                                                                        longitude + 0.05);
        ServerResponseMessage serverResponseMessage = new ServerResponseMessage();
        serverResponseMessage.setUsers((ArrayList<UserDto>)users);
        return serverResponseMessage;
    }


    @GetMapping("/userLogin")
    public String userLogin(@RequestParam ("username") String username,
                            @RequestParam ("password") String password){
        return "/userLogin";
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam ("username") String username,
                               @RequestParam ("password") String password,
                               @RequestParam ("firstName") String firstName,
                               @RequestParam ("lastName") String lastName,
                               @RequestParam ("latitude") double latitude,
                               @RequestParam ("longitude") double longitude){
        return "/register";
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam ("username") String username,
                             @RequestParam ("latitude") double latitute,
                             @RequestParam ("longitude") double longitude){
        return "/update";
    }

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam ("fromUser") String fromUsername,
                              @RequestParam ("message") String message,
                              @RequestParam ("toUser") String toUsername){
        return "/sendMessage";
    }

    @GetMapping("/getMessages")
    public String getMessages(@RequestParam ("fromUser") String fromUsername,
                              @RequestParam ("toUser") String toUsername){
        return "/getMessages";
    }

    @GetMapping("/getUsers")
    public String getUsers(@RequestParam ("username") String username){
        return "/getUsers";
    }

}
