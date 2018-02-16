package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 31/01/2017.
 */

public class APIMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static APIMessage create(String message){
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(message);
        return apiMessage;
    }

    @Override
    public String toString() {
        return "APIMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
