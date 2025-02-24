package es.daw01.savex.model;

import org.jetbrains.annotations.NotNull;
import jakarta.validation.constraints.*;
public class UserDTO {
    @NotNull
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must be alphanumeric")
    private String username;

    @NotNull
    @Size(min = 3, max = 50, message = "Email must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9+_.-]", message = "Email must be like example@exdomain.com")
    private String email;

    @NotNull
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one lowercase letter, one uppercase letter and one digit")
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
