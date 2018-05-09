package server.service.message;

import server.dto.MessageDto;
import server.dto.UserDto;

import java.util.List;

public interface MessageService {

    List<MessageDto> findByFromUserAndToUser(UserDto fromUserDto, UserDto toUserDto);

}
