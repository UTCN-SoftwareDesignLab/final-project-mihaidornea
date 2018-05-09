package server.dto.builder;

import server.dto.MessageDto;
import server.dto.UserDto;

public class MessageDtoBuilder {

    private MessageDto messageDto;

    public MessageDtoBuilder(){
        messageDto = new MessageDto();
    }

    public MessageDtoBuilder setContent(String content){
        messageDto.setContent(content);
        return this;
    }

    public MessageDtoBuilder setFromUserDto(UserDto fromUserDto){
        messageDto.setFromUserDto(fromUserDto);
        return this;
    }

    public MessageDtoBuilder setToUserDto(UserDto toUserDto){
        messageDto.setToUserDot(toUserDto);
        return this;
    }

    public MessageDto build(){
        return messageDto;
    }

}
