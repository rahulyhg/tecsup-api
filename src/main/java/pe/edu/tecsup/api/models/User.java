package pe.edu.tecsup.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.tecsup.api.utils.Constant;

import java.util.Collection;

public class User implements UserDetails {

    /* Basic */
    private Integer id;
    private String fullname;

    /* Google */
    private String gid;
    private String name;
    private String email;
    private String picture;

    /* JWT Token */
    private String token;

    /* Sitec */
    private String sede;
    private Integer role;
    private String dni;
    private String tipo;

    /* PhoneNumber */
    private PhoneNumber phoneNumber;

    /* Card ID */
    private CardID cardID = new CardID();

    /* Spring Security related fields*/
    private Collection<Role> authorities;

    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean accountNonLocked;
    @JsonIgnore
    private boolean credentialsNonExpired;
    @JsonIgnore
    private boolean enabled;

    public Collection<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Role> authorities) {
        this.authorities = authorities;

        // Default role
        if(this.getAuthorities() != null) {
            if (this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ADMINISTRADOR)))
                this.setRole(Constant.ROLE_SEVA_ADMINISTRADOR);
            else if (this.getAuthorities().contains(new Role(Constant.ROLE_PORTAL_SOPORTE)))
                this.setRole(Constant.ROLE_PORTAL_SOPORTE);
            else if (this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_SECRETARIA)))
                this.setRole(Constant.ROLE_SEVA_SECRETARIA);
            else if (this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_DIRECTOR)))
                this.setRole(Constant.ROLE_SEVA_DIRECTOR);
            else if (this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_JEFE_DEPARTAMENTO)))
                this.setRole(Constant.ROLE_SEVA_JEFE_DEPARTAMENTO);
            else if (this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_DOCENTE)))
                this.setRole(Constant.ROLE_SEVA_DOCENTE);
            else if (this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE)) || this.getAuthorities().contains(new Role(Constant.ROLE_SEVA_ESTUDIANTE_ANTIGUO)))
                this.setRole(Constant.ROLE_SEVA_ESTUDIANTE);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CardID getCardID() {
        return cardID;
    }

    public void setCardID(CardID cardID) {
        this.cardID = cardID;
    }

    public boolean hasRole(Integer roleid){
        if(authorities != null && roleid != null){
            return authorities.contains(new Role(roleid));
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", gid='" + gid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                ", token='" + token + '\'' +
                ", sede='" + sede + '\'' +
                ", role=" + role +
                ", dni='" + dni + '\'' +
                ", tipo='" + tipo + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", cardID=" + cardID +
                ", authorities=" + authorities +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                '}';
    }
}
