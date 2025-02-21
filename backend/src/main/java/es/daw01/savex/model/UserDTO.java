package es.daw01.savex.model;

import org.jetbrains.annotations.NotNull;


public class UserDTO {
    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    // Constructors ----------------------------------------------------------->>
    public UserDTO() {
        this.email = "";
        this.username = "";
        this.password = "";
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
}
