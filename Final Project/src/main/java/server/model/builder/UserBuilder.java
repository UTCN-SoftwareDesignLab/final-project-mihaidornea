package server.model.builder;

import server.model.User;

public class UserBuilder {

    private User user;

    public UserBuilder() {
        user = new User();
    }

    public UserBuilder setUsername(String username){
        user.setUsername(username);
        return this;
    }

    public UserBuilder setPassword(String password){
        user.setPassword(password);
        return this;
    }

    public UserBuilder setFirstName(String firstName){
        user.setFirstName(firstName);
        return this;
    }

    public UserBuilder setLastName(String lastName){
        user.setLastName(lastName);
        return this;
    }

    public UserBuilder setLatitude(double latitude){
        user.setLatitude(latitude);
        return this;
    }

    public UserBuilder setLongitude(double longitude){
        user.setLongitude(longitude);
        return this;
    }

    public User build(){
        return user;
    }

}
