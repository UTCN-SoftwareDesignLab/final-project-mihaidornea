package server.messages;

public class ServerResponseMessage {

    public String status;

    public ServerResponseMessage(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
