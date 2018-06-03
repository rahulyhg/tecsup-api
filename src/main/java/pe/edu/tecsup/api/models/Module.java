package pe.edu.tecsup.api.models;

public class Module {

    private Integer codmodule;

    private String nommodule;

    private Integer orden;

    private String fecinicio;

    private String fecfin;

    private String aula;

    public Integer getCodmodule() {
        return codmodule;
    }

    public void setCodmodule(Integer codmodule) {
        this.codmodule = codmodule;
    }

    public String getNommodule() {
        return nommodule;
    }

    public void setNommodule(String nommodule) {
        this.nommodule = nommodule;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getFecinicio() {
        return fecinicio;
    }

    public void setFecinicio(String fecinicio) {
        this.fecinicio = fecinicio;
    }

    public String getFecfin() {
        return fecfin;
    }

    public void setFecfin(String fecfin) {
        this.fecfin = fecfin;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    @Override
    public String toString() {
        return "Module{" +
                "codmodule=" + codmodule +
                ", nommodule='" + nommodule + '\'' +
                ", orden=" + orden +
                ", fecinicio='" + fecinicio + '\'' +
                ", fecfin='" + fecfin + '\'' +
                ", aula='" + aula + '\'' +
                '}';
    }
}
