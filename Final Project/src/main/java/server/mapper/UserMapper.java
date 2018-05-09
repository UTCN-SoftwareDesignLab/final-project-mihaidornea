package server.mapper;

import server.dto.UserDto;
import server.dto.builder.UserDtoBuilder;
import server.model.User;
import server.model.builder.UserBuilder;

public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public User mapTo(UserDto userDto) {
        return new UserBuilder()
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setUsername(userDto.getUsername())
                .setPassword(userDto.getPassword())
                .setLatitude(userDto.getLatitude())
                .setLongitude(userDto.getLongitude())
                .build();
    }

    @Override
    public UserDto mapFrom(User user) {
        return new UserDtoBuilder()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setLatitude(user.getLatitude())
                .setLongitude(user.getLongitude())
                .build();
    }
}
