package com.examples.scs.finalprojectclient.messages;

/**
 * Created by mihaidornea on 5/16/2018.
 */

public class ChatMessage {

    public boolean left;
    public String message;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }
}
