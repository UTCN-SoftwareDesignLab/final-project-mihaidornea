package server.dto.builder;

import server.dto.UserDto;

public class UserDtoBuilder {

    private UserDto userDto;

    public UserDtoBuilder(){
        userDto = new UserDto();
    }

    public UserDtoBuilder setUsername(String username){
        userDto.setUsername(username);
        return this;
    }

    public UserDtoBuilder setPassword(String password){
        userDto.setPassword(password);
        return this;
    }

    public UserDtoBuilder setFirstName(String firstName){
        userDto.setFirstName(firstName);
        return this;
    }

    public UserDtoBuilder setLastName(String lastName){
        userDto.setLastName(lastName);
        return this;
    }

    public UserDtoBuilder setLatitude(double latitude){
        userDto.setLatitude(latitude);
        return this;
    }

    public UserDtoBuilder setLongitude(double longitude){
        userDto.setLongitude(longitude);
        return this;
    }

    public UserDto build(){
        return userDto;
    }

}
