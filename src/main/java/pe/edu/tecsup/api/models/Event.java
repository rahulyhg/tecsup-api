package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 15/05/2017.
 */
public class Event {

    public Long id;
    public String course;
    public Integer year;
    public Integer month;
    public Integer day;
    public String startTime;
    public String endTime;
    public String room;
    public String teacher;
    public String section;
    public String type;
    public String frequency;
    public String topic;
    public String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", course='" + course + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", room='" + room + '\'' +
                ", teacher='" + teacher + '\'' +
                ", section='" + section + '\'' +
                ", type='" + type + '\'' +
                ", frequency='" + frequency + '\'' +
                ", topic='" + topic + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}