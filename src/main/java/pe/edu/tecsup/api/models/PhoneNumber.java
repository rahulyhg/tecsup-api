package pe.edu.tecsup.api.models;

public class PhoneNumber{

    private Long id;

    private String instanceid;

    private String countryCode;

    private String phoneNumber;

    private Boolean activated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getFullPhoneNumber(){
        if(this.phoneNumber != null) {
            String fullPhoneNumber = this.phoneNumber;
            if (this.countryCode != null)
                fullPhoneNumber = "+" + countryCode + fullPhoneNumber;
            return fullPhoneNumber;
        }
        return null;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", instanceid='" + instanceid + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", activated=" + activated +
                '}';
    }
}