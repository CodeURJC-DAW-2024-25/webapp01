package es.daw01.savex.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    private Post post;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    private String formatedDate;
    private String content;

    // Constructors ----------------------------------------------------------->>

    protected Comment() {
        /* Used by Spring Data JPA */ }

    public Comment(User author, Post post, String content) {
        this.author = author;
        this.post = post;
        this.content = content;
        this.createdAt = LocalDateTime.now();

        // Format date time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.formatedDate = this.createdAt.format(formatter);
    }

    // Public Methods --------------------------------------------------------->>

    /**
     * Check if the given user is the author of this comment
     * 
     * @param user User to check
     * @return True if the user is the author of this comment, false otherwise
     */
    public boolean isAuthor(User user) {
        return this.author.equals(user) || user.isAdmin();
    }

    /**
     * Remove this comment from the post it belongs to
     */
    public void removeFromPost() {
        if (this.post == null)
            return;

        this.post.removeComment(this);
        this.post = null;
    }

    /**
     * Remove this comment from the author it belongs to
     */
    public void removeFromAuthor() {
        if (this.author == null)
            return;

        this.author.removeComment(this);
        this.author = null;
    }

    /**
     * 
     * Update comment with the new comment data
     */

    public void updateComment(Comment comment) {
        this.content = comment.getContent();
        this.author = comment.getAuthor();
    }

    // Getters and setters ---------------------------------------------------->>

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
