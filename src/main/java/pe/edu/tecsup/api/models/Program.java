package pe.edu.tecsup.api.models;

import java.util.List;

public class Program {

    private Integer codactividad;

    private Integer codfamilia;

    private String nomfamilia;

    private String nomactividad;

    private String fecinicio;

    private String fecfin;

    private String horario;

    private String ambiente;

    private Integer codempresa;

    private String nomempresa;

    private String ruc;

    private List<Module> modules;

    public Integer getCodactividad() {
        return codactividad;
    }

    public void setCodactividad(Integer codactividad) {
        this.codactividad = codactividad;
    }

    public Integer getCodfamilia() {
        return codfamilia;
    }

    public void setCodfamilia(Integer codfamilia) {
        this.codfamilia = codfamilia;
    }

    public String getNomfamilia() {
        return nomfamilia;
    }

    public void setNomfamilia(String nomfamilia) {
        this.nomfamilia = nomfamilia;
    }

    public String getNomactividad() {
        return nomactividad;
    }

    public void setNomactividad(String nomactividad) {
        this.nomactividad = nomactividad;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public Integer getCodempresa() {
        return codempresa;
    }

    public void setCodempresa(Integer codempresa) {
        this.codempresa = codempresa;
    }

    public String getNomempresa() {
        return nomempresa;
    }

    public void setNomempresa(String nomempresa) {
        this.nomempresa = nomempresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "Program{" +
                "codactividad=" + codactividad +
                ", codfamilia='" + codfamilia + '\'' +
                ", nomfamilia='" + nomfamilia + '\'' +
                ", nomactividad='" + nomactividad + '\'' +
                ", fecinicio='" + fecinicio + '\'' +
                ", fecfin='" + fecfin + '\'' +
                ", horario='" + horario + '\'' +
                ", ambiente='" + ambiente + '\'' +
                ", codempresa=" + codempresa +
                ", nomempresa='" + nomempresa + '\'' +
                ", ruc='" + ruc + '\'' +
                ", modules=" + modules +
                '}';
    }
}
