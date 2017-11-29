package pe.edu.tecsup.api.remotes.twilio.models;

public class ResponseChecking {

    private String message;

    private Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseChecking{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
