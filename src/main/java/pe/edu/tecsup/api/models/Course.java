package pe.edu.tecsup.api.models;

import java.util.List;

/**
 * Created by ebenites on 15/05/2017.
 */
public class Course {

    private Long id;
    private String periodo;
    private String course;
    private String section;
    private Integer idteacher;
    private String teacher;
    private List<Section> sections;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getIdteacher() {
        return idteacher;
    }

    public void setIdteacher(Integer idteacher) {
        this.idteacher = idteacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", periodo='" + periodo + '\'' +
                ", course='" + course + '\'' +
                ", section='" + section + '\'' +
                ", idteacher=" + idteacher +
                ", teacher='" + teacher + '\'' +
                ", sections=" + sections +
                '}';
    }
}
