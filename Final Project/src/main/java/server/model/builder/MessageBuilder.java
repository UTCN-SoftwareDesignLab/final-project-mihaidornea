package server.model.builder;

import server.model.Message;
import server.model.User;

import java.util.Date;

public class MessageBuilder {

    private Message message;

    public MessageBuilder(){
        message = new Message();
    }

    public MessageBuilder setContent(String content){
        message.setContent(content);
        return this;
    }

    public MessageBuilder setDate(Date date) {
        message.setDate(date);
        return this;
    }

    public MessageBuilder setFromUser(User fromUser){
        message.setFromUser(fromUser);
        return this;
    }

    public MessageBuilder setToUser(User toUser){
        message.setToUser(toUser);
        return this;
    }

    public Message build(){
        return message;
    }

}
