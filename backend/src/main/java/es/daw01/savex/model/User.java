package es.daw01.savex.model;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", uniqueConstraints = {
    @jakarta.persistence.UniqueConstraint(columnNames = "email"),
    @jakarta.persistence.UniqueConstraint(columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String hashedPassword;

    @Lob
    private Blob avatar;

    @Column(nullable = false)
    private UserType role;

    // Constructors ----------------------------------------------------------->>

    protected User() { /* Used by Spring Data JPA */ }

    public User(
        String email,
        String username,
        String name,
        String hashedPassword,
        Blob avatar,
        UserType role
    ) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.avatar = avatar;
        this.role = role;
    }

    // Getters and setters ---------------------------------------------------->>

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
    }
    
}
