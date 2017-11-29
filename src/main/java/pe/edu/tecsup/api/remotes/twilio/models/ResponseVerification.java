package pe.edu.tecsup.api.remotes.twilio.models;

import com.google.gson.annotations.SerializedName;

public class ResponseVerification {

    private String carrier;

    @SerializedName("is_cellphone")
    private Boolean isCellphone;

    private String message;

    @SerializedName("seconds_to_expire")
    private Integer secondsToExpire;

    private String uuid;

    private Boolean success;

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Boolean getCellphone() {
        return isCellphone;
    }

    public void setCellphone(Boolean cellphone) {
        isCellphone = cellphone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSecondsToExpire() {
        return secondsToExpire;
    }

    public void setSecondsToExpire(Integer secondsToExpire) {
        this.secondsToExpire = secondsToExpire;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseVerification{" +
                "carrier='" + carrier + '\'' +
                ", isCellphone=" + isCellphone +
                ", message='" + message + '\'' +
                ", secondsToExpire=" + secondsToExpire +
                ", uuid='" + uuid + '\'' +
                ", success=" + success +
                '}';
    }
}
