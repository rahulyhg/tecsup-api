package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 11/04/2017.
 */
public class Debt {

    private String concept;

    private String expiration;

    private Boolean expired;

    private String currency;

    private Double balance;

    private Double arrears;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(Double arrears) {
        this.arrears = arrears;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "concept='" + concept + '\'' +
                ", expiration='" + expiration + '\'' +
                ", expired=" + expired +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", arrears=" + arrears +
                '}';
    }
}
