package es.daw01.savex.model;

public class CommentDTO {
    private Long id;
    private User author;
    private boolean canDelete;
    private String content;
    private String formatedDate;

    // Constructors ----------------------------------------------------------->>

    public CommentDTO() { }

    public CommentDTO(Comment rawComment, User user) {
        this.id = rawComment.getId();
        this.author = rawComment.getAuthor();
        this.canDelete = rawComment.getAuthor().equals(user);
        this.content = rawComment.getContent();
        this.formatedDate = rawComment.getFormatedDate();
    }

    // Getters and setters ---------------------------------------------------->>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
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