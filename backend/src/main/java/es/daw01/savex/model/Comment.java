package es.daw01.savex.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    private User author;
    
    @ManyToOne
    private Post post;
    
    private Date date;
    private String formatedDate;
    private String content;

    // Constructors ----------------------------------------------------------->>

    protected Comment() { /* Used by Spring Data JPA */ }

    public Comment(User author, Post post, String content) {
        this.author = author;
        this.post = post;
        this.content = content;
        this.date = new Date(System.currentTimeMillis());
        this.formatedDate = this.date.toString();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
