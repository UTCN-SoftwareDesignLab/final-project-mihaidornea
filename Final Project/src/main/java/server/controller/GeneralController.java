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
import server.dto.UserDto;
import server.dto.builder.UserDtoBuilder;
import server.messages.GeneralMessage;
import server.messages.ServerResponseMessage;
import server.service.message.MessageService;
import server.service.user.UserService;

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
        String response = "";
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
    }

    @MessageMapping("/login/{username}")
    @SendTo("/broker/{username}")
    public ServerResponseMessage login(@DestinationVariable String username, Message<GeneralMessage> message1) throws Exception{
        String message = message1.getPayload().getContent();
        try {
            String[] fields = message.split("/");
            UserDto userDto = userService.findByUsername(fields[0]);
            if (userDto == null) {
                return new ServerResponseMessage("Incorrect username!");
            } else if (!userDto.getPassword().equals(fields[1])){
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
            String[] fields = message.getPayload().getContent().split("/");
            System.out.println(message.getPayload().getContent());
            response = fields[0];
            String toUser = fields[1];
            UserDto fromUserDto = userService.findByUsername(username);
            UserDto toUserDto = userService.findByUsername(toUser);
            messageService.create(fromUserDto, toUserDto, fields[0]);
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return new ServerResponseMessage(response);
    }


    private String register(String message){
        try {
            String[] fields = message.split("/");
            UserDto userDto = new UserDtoBuilder()
                    .setFirstName(fields[0])
                    .setLastName(fields[1])
                    .setUsername(fields[2])
                    .setPassword(fields[3])
                    .setLatitude(Double.parseDouble(fields[4]))
                    .setLongitude(Double.parseDouble(fields[5]))
                    .build();
            if (userService.create(userDto))
                return "User Successfully Created!";
            else return "Error when creating the user!";
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return "Error when creating the user!";
    }
    private String update(String message){
        try {
            String[] fields = message.split("/");
            UserDto userDto = userService.findByUsername(fields[0]);
            if (userDto != null) {
                if(userService.update(userDto, Double.parseDouble(fields[1]), Double.parseDouble(fields[2])))
                    return "Update Successfully!";
                else return "Failed to Update!";
            } else return "Invalid username!";
        } catch (ArrayIndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
        return "Failed to update!";
    }


    @GetMapping("/userLogin")
    public String userLogin(@RequestParam ("username") String username, @RequestParam ("password") String password){
        return "/userLogin";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "/login";
    }

    @GetMapping("/register")
    public String registerForm(){
        return "/register";
    }

    @GetMapping("/update")
    public String updateForm(){
        return "/update";
    }

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam ("username") String username){
        return "/sendMessage";
    }

}
