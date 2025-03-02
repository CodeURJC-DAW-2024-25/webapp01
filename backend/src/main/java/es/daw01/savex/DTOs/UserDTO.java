package es.daw01.savex.DTOs;

import es.daw01.savex.model.User;
import jakarta.validation.constraints.*;

public class UserDTO {
    private long id;
    
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "El nombre de usuario solo puede contener letras y números")
    private String username;

    @Size(min = 3, max = 50, message = "El email debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9+_.-]", message = "El email debe tener un formato válido: mail@domain.com")
    private String email;

    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe contener al menos una letra mayúscula, una letra minúscula y un número")
    private String password;

    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "El nombre solo puede contener letras")
    private String name;

    private String role;
    private int nComments;


    // Constructors ----------------------------------------------------------->>
    public UserDTO() {
        this.email = "";
        this.username = "";
        this.password = "";
        this.name = "";
        this.role = "";
        this.nComments = 0;
        this.id = 0;
    }

    public UserDTO(long id, String username, String email, String password, String name, String role, int nComment) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.nComments = nComment;

    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getHashedPassword();
        this.name = user.getName();
        this.role = user.getRole().toString();
        this.nComments = user.getComments().size();
    }

    // Getters and setters ---------------------------------------------------->>
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getnComments() {
        return nComments;
    }

    public void setnComments(int nComment) {
        this.nComments = nComment;
    }

}
