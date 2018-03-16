package pe.edu.tecsup.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ebenites on 7/05/2017.
 */

@Entity
@Table(name="API_NEWS")
public class New {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NEWS")
    @SequenceGenerator(sequenceName = "SEQ_NEWS", allocationSize = 1, name = "SEQ_NEWS")
    private Long id;

    @NotEmpty(message = "Campo título mandatorio")
    @Column(length = 100, nullable = false)
    private String title;

    @NotEmpty(message = "Campo resumen mandatorio")
    @Column(length = 500, nullable = false)
    private String summary;

    @NotEmpty(message = "Campo contenido mandatorio")
    @Column(nullable = false)
    private String content;

    @NotNull(message = "Campo fecha mandatorio")
    @JsonFormat(pattern="dd/MM/yyyy")
    @Column(nullable = false)
    private Date published;

    @NotEmpty(message = "Campo imagen mandatorio")
    @Column(length = 255)
    private String picture;

    @Column(length = 1, columnDefinition="CHAR")
    private String sede;

    @Column(length = 1)
    private Integer activated;

    @Column(length = 1)
    private Integer deleted;

    @Column(name = "CREATED_AT")
    private Date createdat;
    @Column(name = "CREATED_BY", length = 20)
    private String createdby;
    @Column(name = "UPDATED_AT")
    private Date updatedat;
    @Column(name = "UPDATED_BY", length = 20)
    private String updatedby;

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

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createby) {
        this.createdby = createby;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getSedeFullname(){
        if(null == this.sede){
            return "Todas las sedes";
        }else if("L".equals(this.sede)){
            return "Lima";
        }else if("A".equals(this.sede)){
            return "Arequipa";
        }else if("T".equals(this.sede)){
            return "Trujillo";
        }else if("H".equals(this.sede)){
            return "Huancayo";
        }
        return this.sede;
    }

    @Override
    public String toString() {
        return "New{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", published=" + published +
                ", picture='" + picture + '\'' +
                ", sede='" + sede + '\'' +
                ", activated=" + activated +
                ", deleted=" + deleted +
                ", createdat=" + createdat +
                ", createdby='" + createdby + '\'' +
                ", updatedat=" + updatedat +
                ", updatedby='" + updatedby + '\'' +
                '}';
    }
}
