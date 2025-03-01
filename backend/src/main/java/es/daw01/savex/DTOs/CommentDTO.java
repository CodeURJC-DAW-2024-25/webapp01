package es.daw01.savex.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.daw01.savex.model.Comment;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;

public class CommentDTO {
    private Long id;
    private AuthorDTO author;
    private boolean canDelete;
    private String content;
    private String formatedDate;

    // Constructors ----------------------------------------------------------->>

    public CommentDTO() { }

    public CommentDTO(Comment rawComment, User user) {
        this.id = rawComment.getId();
        this.author = new AuthorDTO(rawComment.getAuthor());
        this.content = rawComment.getContent();
        this.formatedDate = rawComment.getFormatedDate();
        
        if (user != null) {
            this.canDelete = rawComment.isAuthor(user) || user.getRole() == UserType.ADMIN;
        } else this.canDelete = false;
    }

    // Inner classes ---------------------------------------------------------->>
    private class AuthorDTO {
        private String username;
        
        public AuthorDTO(User author) {
            this.username = (author != null)
                ? author.getUsername()
                : "Anonymous";
        }
        
        @JsonProperty("username")
        public String getUsername() {
            return username;
        }        
    }

    // Getters and setters ---------------------------------------------------->>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }
}