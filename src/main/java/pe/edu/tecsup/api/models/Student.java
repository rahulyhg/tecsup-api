package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 16/07/2017.
 */
public class Student {

    private Integer id;

    private String carnet;

    private String nombres;

    private Integer codcursoejec;

    private Integer codseccion;

    private String nomseccion;

    private String correo;

    private String instanceid;

    private Score score;

    private Attendance attendance;

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

    public Integer getCodcursoejec() {
        return codcursoejec;
    }

    public void setCodcursoejec(Integer codcursoejec) {
        this.codcursoejec = codcursoejec;
    }

    public Integer getCodseccion() {
        return codseccion;
    }

    public void setCodseccion(Integer codseccion) {
        this.codseccion = codseccion;
    }

    public String getNomseccion() {
        return nomseccion;
    }

    public void setNomseccion(String nomseccion) {
        this.nomseccion = nomseccion;
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

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", carnet='" + carnet + '\'' +
                ", nombres='" + nombres + '\'' +
                ", codcursoejec=" + codcursoejec +
                ", codseccion=" + codseccion +
                ", nomseccion='" + nomseccion + '\'' +
                ", correo='" + correo + '\'' +
                ", instanceid='" + instanceid + '\'' +
                ", score=" + score +
                ", attendance=" + attendance +
                '}';
    }
}
