package pe.edu.tecsup.api.models;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by ebenites on 23/09/2016.
 */
public class Role implements GrantedAuthority {

    private Integer id;

    private String name;

    public Role() {
    }

    public Role(Integer id) {
        this.id = id;
    }

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

    public String getAuthority() { // Role name Authority
        return name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this.id != null && obj!= null && obj instanceof Role){
            Role role = (Role)obj;
            return this.id.equals(role.getId());
        }
        return super.equals(obj);
    }
}
