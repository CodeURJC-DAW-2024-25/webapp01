package es.daw01.savex.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String title;
    private String description;
    private String content;
    private String author;
    private String date;
    private VisibilityType visibility;
    private List<String> tags;

    @Lob
    private Blob banner;

    // Constructors ----------------------------------------------------------->>

    protected Post() { /* Used by Spring Data JPA */ }

    public Post(
        String title,
        String description,
        String content,
        String author,
        String date,
        VisibilityType visibility,
        List<String> tags,
        Blob banner
    ) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.author = author;
        this.date = date;
        this.visibility = visibility;
        this.tags = tags;
        this.banner = banner;
    }

    // Getters and setters ---------------------------------------------------->>

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public Blob getBanner() {
        return banner;
    }

    public void setBanner(Blob banner) {
        this.banner = banner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
