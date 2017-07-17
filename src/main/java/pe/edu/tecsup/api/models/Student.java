package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 16/07/2017.
 */
public class Student {

    private Integer id;

    private String carnet;

    private String nombres;

    private String correo;

    private String instanceid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", carnet='" + carnet + '\'' +
                ", nombres='" + nombres + '\'' +
                ", correo='" + correo + '\'' +
                ", instanceid='" + instanceid + '\'' +
                '}';
    }
}
