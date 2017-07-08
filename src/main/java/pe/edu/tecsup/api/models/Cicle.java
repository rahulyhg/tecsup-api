package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 8/07/2017.
 */
public class Cicle {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
