package pe.edu.tecsup.api.models;

import java.util.List;

public class CourseDetail {

    private Course course;

    private List<String> assignments;

    private List<Student> students;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "CourseDetail{" +
                "course=" + course +
                ", assignments=" + assignments +
                ", students=" + students +
                '}';
    }
}
