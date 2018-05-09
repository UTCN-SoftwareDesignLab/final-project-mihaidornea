package server.service.user;

import server.dto.UserDto;
import server.model.User;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);
    List<UserDto> findByLatitudeBetweenAndLongitudeBetween(double latitudeMin,
                                                        double latitudeMax,
                                                        double longitudeMin,
                                                        double longitudeMax);
    boolean update(UserDto userDto, double newLatitude, double newLongitude);
    boolean create(UserDto userDto);
    boolean delete(UserDto userDto);

}
