package server.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dto.UserDto;
import server.mapper.Mapper;
import server.mapper.UserMapper;
import server.model.User;
import server.model.builder.UserBuilder;
import server.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private Mapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new UserMapper();
    }

    private User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.get()!=null){
            return optionalUser.get();
        } else
            return null;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user!=null)
            return (UserDto)mapper.mapFrom(user);
        else
            return null;
    }

    @Override
    public List<UserDto> findByLatitudeBetweenAndLongitudeBetween(double latitudeMin, double latitudeMax, double longitudeMin, double longitudeMax) {
        List<User> users = userRepository.findByLatitudeBetweenAndLongitudeBetween(latitudeMin, latitudeMax, longitudeMin, longitudeMax);
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users){
            userDtoList.add((UserDto)mapper.mapFrom(user));
        }
        return userDtoList;
    }

    @Override
    public boolean update(UserDto userDto, double newLatitude, double newLongitude) {
        userRepository.updateLocation(newLatitude, newLongitude, userDto.getUsername());
        return true;
    }

    @Override
    public boolean create(UserDto userDto) {
        userRepository.save((User)mapper.mapTo(userDto));
        return true;
    }

    @Override
    public boolean delete(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }
}
