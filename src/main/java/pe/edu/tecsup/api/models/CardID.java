package pe.edu.tecsup.api.models;

public class CardID {

    private Integer id;

    private Boolean active;

    private String expiration;

    private String porduct;

    private String schedule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getPorduct() {
        return porduct;
    }

    public void setPorduct(String porduct) {
        this.porduct = porduct;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "CardID{" +
                "id=" + id +
                ", active=" + active +
                ", expiration='" + expiration + '\'' +
                ", porduct='" + porduct + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }
}
