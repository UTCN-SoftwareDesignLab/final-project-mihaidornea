package server.messages;

public class GeneralMessage {

    private Content content;

    public GeneralMessage() {
    }

    public GeneralMessage(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
