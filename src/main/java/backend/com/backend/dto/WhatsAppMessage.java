package backend.com.backend.dto;

public class WhatsAppMessage {

    private String from;
    private String message;

    public WhatsAppMessage() {
    }

    public WhatsAppMessage(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
