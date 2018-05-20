package server.dto;

import java.util.Date;

public class MessageDto {

    private String content;
    private UserDto fromUserDto;
    private UserDto toUserDot;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDto getFromUserDto() {
        return fromUserDto;
    }

    public void setFromUserDto(UserDto fromUserDto) {
        this.fromUserDto = fromUserDto;
    }

    public UserDto getToUserDot() {
        return toUserDot;
    }

    public void setToUserDot(UserDto toUserDot) {
        this.toUserDot = toUserDot;
    }
}
