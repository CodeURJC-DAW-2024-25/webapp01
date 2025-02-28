package es.daw01.savex.DTOs;

import jakarta.validation.constraints.*;


public class UserDTO {
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
    @Pattern(regexp = "^[a-zA-Z]*$", message = "El nombre solo puede contener letras")
    private String name;


    // Constructors ----------------------------------------------------------->>
    public UserDTO() {
        this.email = "";
        this.username = "";
        this.password = "";
        this.name = "";
    }

    // Getters and setters ---------------------------------------------------->>

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

}
