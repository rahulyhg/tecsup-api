package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 11/04/2017.
 */
public class Debt {

    private String concept;

    private String expiration;

    private Boolean expired;

    private String balance;

    private String arrears;

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getArrears() {
        return arrears;
    }

    public void setArrears(String arrears) {
        this.arrears = arrears;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "concept='" + concept + '\'' +
                ", expiration='" + expiration + '\'' +
                ", expired=" + expired +
                ", balance='" + balance + '\'' +
                ", arrears='" + arrears + '\'' +
                '}';
    }
}
