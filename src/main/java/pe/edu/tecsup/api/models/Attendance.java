package pe.edu.tecsup.api.models;

import java.util.List;

/**
 * Created by ebenites on 15/05/2017.
 */
public class Attendance {

    private String course;
    private String term;
    private Integer theoId;
    private String theoTitle;
    private Integer theoAttendeds;
    private Integer theoUnattendeds;
    private Integer theoTardiness;
    private Integer labId;
    private String labTitle;
    private Integer labAttendeds;
    private Integer labUnattendeds;
    private Integer labTardiness;
    private Integer workId;
    private String workTitle;
    private Integer workAttendeds;
    private Integer workUnattendeds;
    private Integer workTardiness;
    private List<Item> theos;
    private List<Item> labs;
    private List<Item> works;
    private Double unattendeds;
    private Boolean hasDI;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getTheoId() {
        return theoId;
    }

    public void setTheoId(Integer theoId) {
        this.theoId = theoId;
    }

    public String getTheoTitle() {
        return theoTitle;
    }

    public void setTheoTitle(String theoTitle) {
        this.theoTitle = theoTitle;
    }

    public Integer getTheoAttendeds() {
        return theoAttendeds;
    }

    public void setTheoAttendeds(Integer theoAttendeds) {
        this.theoAttendeds = theoAttendeds;
    }

    public Integer getTheoUnattendeds() {
        return theoUnattendeds;
    }

    public void setTheoUnattendeds(Integer theoUnattendeds) {
        this.theoUnattendeds = theoUnattendeds;
    }

    public Integer getTheoTardiness() {
        return theoTardiness;
    }

    public void setTheoTardiness(Integer theoTardiness) {
        this.theoTardiness = theoTardiness;
    }

    public Integer getLabId() {
        return labId;
    }

    public void setLabId(Integer labId) {
        this.labId = labId;
    }

    public String getLabTitle() {
        return labTitle;
    }

    public void setLabTitle(String labTitle) {
        this.labTitle = labTitle;
    }

    public Integer getLabAttendeds() {
        return labAttendeds;
    }

    public void setLabAttendeds(Integer labAttendeds) {
        this.labAttendeds = labAttendeds;
    }

    public Integer getLabUnattendeds() {
        return labUnattendeds;
    }

    public void setLabUnattendeds(Integer labUnattendeds) {
        this.labUnattendeds = labUnattendeds;
    }

    public Integer getLabTardiness() {
        return labTardiness;
    }

    public void setLabTardiness(Integer labTardiness) {
        this.labTardiness = labTardiness;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public Integer getWorkAttendeds() {
        return workAttendeds;
    }

    public void setWorkAttendeds(Integer workAttendeds) {
        this.workAttendeds = workAttendeds;
    }

    public Integer getWorkUnattendeds() {
        return workUnattendeds;
    }

    public void setWorkUnattendeds(Integer workUnattendeds) {
        this.workUnattendeds = workUnattendeds;
    }

    public Integer getWorkTardiness() {
        return workTardiness;
    }

    public void setWorkTardiness(Integer workTardiness) {
        this.workTardiness = workTardiness;
    }

    public List<Item> getTheos() {
        return theos;
    }

    public void setTheos(List<Item> theos) {
        this.theos = theos;
    }

    public List<Item> getLabs() {
        return labs;
    }

    public void setLabs(List<Item> labs) {
        this.labs = labs;
    }

    public List<Item> getWorks() {
        return works;
    }

    public void setWorks(List<Item> works) {
        this.works = works;
    }

    public Double getUnattendeds() {
        return unattendeds;
    }

    public void setUnattendeds(Double unattendeds) {
        this.unattendeds = unattendeds;
    }

    public Boolean getHasDI() {
        return hasDI;
    }

    public void setHasDI(Boolean hasDI) {
        this.hasDI = hasDI;
    }

    public static class Item {
        private String title;
        private String date;
        private String status;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    ", date='" + date + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "course='" + course + '\'' +
                ", term='" + term + '\'' +
                ", theoId=" + theoId +
                ", theoTitle='" + theoTitle + '\'' +
                ", theoAttendeds=" + theoAttendeds +
                ", theoUnattendeds=" + theoUnattendeds +
                ", theoTardiness=" + theoTardiness +
                ", labId=" + labId +
                ", labTitle='" + labTitle + '\'' +
                ", labAttendeds=" + labAttendeds +
                ", labUnattendeds=" + labUnattendeds +
                ", labTardiness=" + labTardiness +
                ", workId=" + workId +
                ", workTitle='" + workTitle + '\'' +
                ", workAttendeds=" + workAttendeds +
                ", workUnattendeds=" + workUnattendeds +
                ", workTardiness=" + workTardiness +
                ", theos=" + theos +
                ", labs=" + labs +
                ", works=" + works +
                ", unattendeds=" + unattendeds +
                ", hasDI=" + hasDI +
                '}';
    }
}
