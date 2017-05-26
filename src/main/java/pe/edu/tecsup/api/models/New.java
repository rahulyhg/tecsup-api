package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 7/05/2017.
 */

public class New {

    private Long id;
    private String title;
    private String summary;
    private String content;
    private Boolean picture;
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getPicture() {
        return picture;
    }

    public void setPicture(Boolean picture) {
        this.picture = picture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "New{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", picture=" + picture +
                ", date='" + date + '\'' +
                '}';
    }
}
