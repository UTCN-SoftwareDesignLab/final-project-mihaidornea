package server.dto;

public class MessageDto {

    private String content;
    private UserDto fromUserDto;
    private UserDto toUserDot;

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
