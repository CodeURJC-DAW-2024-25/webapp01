package es.daw01.savex.DTOs;

import java.util.List;

import es.daw01.savex.model.Post;
import es.daw01.savex.model.VisibilityType;

public class PostDTO {
    private long id;
    private String title;
    private String description;
    private List<CommentDTO> comments;
    private String author;
    private String date;
    private String readingTime;
    private VisibilityType visibility;
    private List<String> tags;

    // Constructors ----------------------------------------------------------->>
    public PostDTO() { }

    public PostDTO(
        long id,
        String title,
        String description, 
        List<CommentDTO> comments,
        String author,
        String date,
        String readingTime,
        VisibilityType visibility,
        List<String> tags
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.comments = comments;
        this.author = author;
        this.date = date;
        this.readingTime = readingTime;
        this.visibility = visibility;
        this.tags = tags;
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.author = post.getAuthor();
        this.date = post.getDate();
        this.readingTime = post.getReadingTime();
        this.visibility = post.getVisibility();
        this.tags = post.getTags();
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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
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

    public String getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(String readingTime) {
        this.readingTime = readingTime;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
