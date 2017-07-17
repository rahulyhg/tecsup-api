package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 9/05/2017.
 */

public class Alert {

    private Long id;
    private String from;
    private String content;
    private Boolean viewed;
    private String date;
    private String to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", content='" + content + '\'' +
                ", viewed=" + viewed +
                ", date='" + date + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
