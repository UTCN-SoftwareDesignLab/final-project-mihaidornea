package server.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dto.MessageDto;
import server.dto.UserDto;
import server.mapper.Mapper;
import server.mapper.MessageMapper;
import server.mapper.UserMapper;
import server.model.Message;
import server.model.User;
import server.model.builder.MessageBuilder;
import server.repository.MessageRepository;
import server.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private Mapper messageMapper;
    private Mapper userMapper;
    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageMapper = new MessageMapper();
        this.userMapper = new UserMapper();
    }

    @Override
    public List<MessageDto> findByFromUserAndToUser(UserDto fromUserDto, UserDto toUserDto) {
        User fromUser = userRepository.findByUsername(fromUserDto.getUsername());
        User toUser = userRepository.findByUsername(toUserDto.getUsername());
        List<Message> messages = messageRepository.findByFromUserAndToUser(fromUser, toUser);
        List<MessageDto> messageDtoList = new ArrayList<>();
        for(Message message : messages){
            messageDtoList.add((MessageDto)messageMapper.mapFrom(message));
        }
        return messageDtoList;
    }

    @Override
    public boolean create(UserDto fromUserDto, UserDto toUserDto, String message) {
        User fromUser = userRepository.findByUsername(fromUserDto.getUsername());
        User toUser = userRepository.findByUsername(toUserDto.getUsername());
        System.out.println(fromUser.getId()  + " " + toUser.getId());

        Message message1 = new MessageBuilder()
                .setFromUser(fromUser)
                .setToUser(toUser)
                .setContent(message)
                .build();
        messageRepository.save(message1);
        return true;
    }
}
