package pe.edu.tecsup.api.models;

import java.util.List;

public class Customer {

    private Integer id;

    private String fullname;

    private String name;

    private String email;

    private String sede;

    private List<String> instancesid;

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

    public List<String> getInstancesid() {
        return instancesid;
    }

    public void setInstancesid(List<String> instancesid) {
        this.instancesid = instancesid;
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
        return "Customer{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", sede='" + sede + '\'' +
                ", instancesid=" + instancesid +
                ", status=" + status +
                '}';
    }
}
