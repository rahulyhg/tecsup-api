package pe.edu.tecsup.api.models;

import java.util.List;

public class CourseDashboard {

    private List<Score> scores;

    private List<Attendance> attendances;

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    @Override
    public String toString() {
        return "CourseDashboard{" +
                "scores=" + scores +
                ", attendances=" + attendances +
                '}';
    }

    public static class Score {

        private String date;
        private Integer codtype;
        private Integer number;
        private Double score;
        private Integer total;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getCodtype() {
            return codtype;
        }

        public void setCodtype(Integer codtype) {
            this.codtype = codtype;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Score{" +
                    "date='" + date + '\'' +
                    ", codtype=" + codtype +
                    ", number=" + number +
                    ", score=" + score +
                    ", total=" + total +
                    '}';
        }
    }

    public static class Attendance {

        private String date;
        private Integer codtype;
        private Integer number;
        private Integer attendeds;
        private Integer unattendeds;
        private Integer tardiness;
        private Integer total;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getCodtype() {
            return codtype;
        }

        public void setCodtype(Integer codtype) {
            this.codtype = codtype;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getAttendeds() {
            return attendeds;
        }

        public void setAttendeds(Integer attendeds) {
            this.attendeds = attendeds;
        }

        public Integer getUnattendeds() {
            return unattendeds;
        }

        public void setUnattendeds(Integer unattendeds) {
            this.unattendeds = unattendeds;
        }

        public Integer getTardiness() {
            return tardiness;
        }

        public void setTardiness(Integer tardiness) {
            this.tardiness = tardiness;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Attendance{" +
                    "date='" + date + '\'' +
                    ", codtype=" + codtype +
                    ", number=" + number +
                    ", attendeds=" + attendeds +
                    ", unattendeds=" + unattendeds +
                    ", tardiness=" + tardiness +
                    ", total=" + total +
                    '}';
        }
    }

}
