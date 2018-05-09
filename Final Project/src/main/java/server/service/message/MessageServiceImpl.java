package server.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dto.MessageDto;
import server.dto.UserDto;
import server.mapper.Mapper;
import server.mapper.MessageMapper;
import server.model.Message;
import server.model.User;
import server.repository.MessageRepository;
import server.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private Mapper mapper;
    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.mapper = new MessageMapper();
    }

    @Override
    public List<MessageDto> findByFromUserAndToUser(UserDto fromUserDto, UserDto toUserDto) {
        User fromUser = userRepository.findByUsername(fromUserDto.getUsername());
        User toUser = userRepository.findByUsername(toUserDto.getUsername());
        List<Message> messages = messageRepository.findByFromUserAndToUser(fromUser, toUser);
        List<MessageDto> messageDtoList = new ArrayList<>();
        for(Message message : messages){
            messageDtoList.add((MessageDto)mapper.mapFrom(message));
        }
        return messageDtoList;
    }
}
