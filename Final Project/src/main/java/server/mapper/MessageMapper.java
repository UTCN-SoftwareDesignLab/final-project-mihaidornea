package server.mapper;

import server.dto.MessageDto;
import server.dto.UserDto;
import server.dto.builder.MessageDtoBuilder;
import server.model.Message;
import server.model.User;
import server.model.builder.MessageBuilder;

public class MessageMapper implements Mapper<Message, MessageDto> {

    private Mapper userMapper = new UserMapper();

    @Override
    public Message mapTo(MessageDto messageDto) {
        return new MessageBuilder()
                .setContent(messageDto.getContent())
                .setFromUser((User)userMapper.mapTo(messageDto.getFromUserDto()))
                .setToUser((User)userMapper.mapTo(messageDto.getFromUserDto()))
                .build();
    }

    @Override
    public MessageDto mapFrom(Message message) {
        return new MessageDtoBuilder()
                .setContent(message.getContent())
                .setFromUserDto((UserDto)userMapper.mapFrom(message.getFromUser()))
                .setToUserDto((UserDto)userMapper.mapFrom(message.getToUser()))
                .build();
    }
}
