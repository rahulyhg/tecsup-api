package pe.edu.tecsup.api.models;

import java.util.List;

/**
 * Created by ebenites on 7/05/2017.
 */

public class History {

    private Long idstudent;
    private String fullname;
    private Integer idcareer;
    private String career;
    private Double averrage;
    private Integer ranking;
    private String status;
    private List<Cycle> cycles;

    public Long getIdstudent() {
        return idstudent;
    }

    public void setIdstudent(Long idstudent) {
        this.idstudent = idstudent;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getIdcareer() {
        return idcareer;
    }

    public void setIdcareer(Integer idcareer) {
        this.idcareer = idcareer;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public Double getAverrage() {
        return averrage;
    }

    public void setAverrage(Double averrage) {
        this.averrage = averrage;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<Cycle> cycles) {
        this.cycles = cycles;
    }

    public static class Course {
        private String name;
        private Double score;
        private Boolean status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Course{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    ", status=" + status +
                    '}';
        }
    }

    public static class Cycle {
        private String name;
        private Integer idterm;
        private String term;
        private Double averrage;
        private List<Course> courses;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getIdterm() {
            return idterm;
        }

        public void setIdterm(Integer idterm) {
            this.idterm = idterm;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public Double getAverrage() {
            return averrage;
        }

        public void setAverrage(Double averrage) {
            this.averrage = averrage;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        @Override
        public String toString() {
            return "Cycle{" +
                    "name='" + name + '\'' +
                    ", idterm=" + idterm +
                    ", term='" + term + '\'' +
                    ", averrage=" + averrage +
                    ", courses=" + courses +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "History{" +
                "idstudent=" + idstudent +
                ", fullname='" + fullname + '\'' +
                ", idcareer=" + idcareer +
                ", career='" + career + '\'' +
                ", averrage=" + averrage +
                ", ranking=" + ranking +
                ", status='" + status + '\'' +
                ", cycles=" + cycles +
                '}';
    }
}
