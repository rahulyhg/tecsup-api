package pe.edu.tecsup.api.models;

import java.util.Date;

public class Technical {

    private Integer id;

    private String fullname;

    private String name;

    private String email;

    private String sede;

    private Date starttime;

    private Date endtime;

    private String instanceid;

    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Technical{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", sede='" + sede + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", instanceid='" + instanceid + '\'' +
                ", status=" + status +
                '}';
    }
}
