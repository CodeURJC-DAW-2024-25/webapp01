package es.daw01.savex.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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

    @ElementCollection
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @ElementCollection
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShoppingList> shoppingLists;

    // Constructors ----------------------------------------------------------->>

    public User() {
        /* Used by Spring Data JPA */
    }

    public User(
            String email,
            String username,
            String name,
            String hashedPassword,
            Blob avatar,
            UserType role) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.avatar = avatar;
        this.role = role;
    }

    // Functions -------------------------------------------------------------->>

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;

        User user = (User) obj;
        if (user.id == this.id)
            return true;
        if (user.username.equals(this.username))
            return true;
        if (user.email.equals(this.email))
            return true;

        return false;
    }

    /**
     * Check if the user is an admin
     * 
     * @return True if the user is an admin, false otherwise
    */
    public boolean isAdmin() {
        return this.role == UserType.ADMIN;
    }

    /**
     * Add a comment to the user
     * 
     * @param comment The comment to add
    */
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    /**
     * Remove a comment from the user
     * 
     * @param comment The comment to remove
    */
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
