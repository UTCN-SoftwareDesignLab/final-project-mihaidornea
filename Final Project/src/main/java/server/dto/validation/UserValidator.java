package server.dto.validation;

import server.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_USERNAME_LENGTH = 5;

    private final UserDto userDto;

    private final List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public UserValidator(UserDto userDto) {
        this.userDto = userDto;
        this.errors = new ArrayList<>();
    }

    public boolean validate(){
        validatePassword(userDto.getPassword());
        validateUsername(userDto.getUsername());
        return errors.isEmpty();
    }

    private void validateUsername(String username){
        if (username.length() < MIN_USERNAME_LENGTH){
            errors.add("Username too short!");
        }
    }

    private void validatePassword(String password){
        if (password.length() < MIN_PASSWORD_LENGTH){
            errors.add("Password too short!");
        }

        if (containsSpecialCharacter(password)) {
            errors.add("Passwod must contain at least one special character!");
        }

        if (!containsDigit(password)) {
            errors.add("Password must contain at least one number!");
        }
    }

    private boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("^A-Za-z0-9");
        Matcher m = p.matcher(s);
        return m.find();
    }

    private boolean containsDigit(String s){
        if (s != null && !s.isEmpty()){
            for (char c : s.toCharArray()){
                if(Character.isDigit(c)){
                    return true;
                }
            }
        }
        return false;
    }

}
