package server.messages;

import server.dto.UserDto;

import java.util.ArrayList;

public class ServerResponseMessage {

    private String status;
    private ArrayList<IMessage> messages;
    private ArrayList<UserDto> users;


    public ServerResponseMessage() {
    }

    public ServerResponseMessage(String status) {
        this.status = status;
    }

    public ServerResponseMessage(String status, ArrayList<IMessage> messages) {
        this.status = status;
        this.messages = messages;
    }

    public ServerResponseMessage(String status, ArrayList<IMessage> messages, ArrayList<UserDto> users) {
        this.status = status;
        this.messages = messages;
        this.users = users;
    }

    public ArrayList<IMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<IMessage> messages) {
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<UserDto> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserDto> users) {
        this.users = users;
    }
}
