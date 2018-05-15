package server.messages;

public class GeneralMessage {

    private String content;

    public GeneralMessage() {
    }

    public GeneralMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
