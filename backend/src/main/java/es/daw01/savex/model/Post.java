package es.daw01.savex.model;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String readingTime;

    @Column(nullable = false)
    private VisibilityType visibility;

    @Column(nullable = false)
    private List<String> tags;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Lob
    private Blob banner;

    // Constructors ----------------------------------------------------------->>

    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public Post(String markdownContent, Map<String, List<String>> yamlFrontMatter) {
        this.title = yamlFrontMatter.get("title").get(0);
        this.description = yamlFrontMatter.get("description").get(0);
        this.content = markdownContent;
        this.author = yamlFrontMatter.get("author").get(0);
        this.date = yamlFrontMatter.get("date").get(0);
        this.readingTime = yamlFrontMatter.get("reading_time").get(0);
        this.visibility = VisibilityType.valueOf(yamlFrontMatter.get("visibility").get(0).toUpperCase());
        this.tags = yamlFrontMatter.get("tags");
        this.createdAt = LocalDateTime.now();
        this.setBanner(yamlFrontMatter.get("banner").get(0));
    }

    public Post(
            String title,
            String description,
            String content,
            String author,
            String date,
            String readingTime,
            VisibilityType visibility,
            List<String> tags,
            Blob banner) {
        this.createdAt = LocalDateTime.now();
        this.title = title;
        this.description = description;
        this.content = content;
        this.author = author;
        this.date = date;
        this.readingTime = readingTime;
        this.visibility = visibility;
        this.tags = tags;
        this.banner = banner;
    }

    // Functions -------------------------------------------------------------->>

    /**
     * Save the image file as the post banner
     * 
     * @param imageFile the image file to save as the post banner
     * @throws IOException if the image file cannot be read
     */
    public void saveImage(MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            this.banner = BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
        }
    }

    /**
     * Add a comment to the post
     * 
     * @param comment the comment to add to the post
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    /**
     * Remove a comment from the post
     * 
     * @param comment the comment to remove from the post
     */
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    /**
     * Check if the user has commented on the post
     * 
     * @param user the user to check if they have commented on the post
     * @return true if the user has commented on the post, false otherwise
     */
    public boolean hasCommented(User user) {
        for (Comment comment : this.getComments()) {
            if (comment.getAuthor().equals(user)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the post is public
     * 
     * @return true if the post is public, false otherwise
     */
    public boolean isPublic() {
        return this.visibility == VisibilityType.PUBLIC;
    }

    /**
     * Calculate the reading time of the post in minutes
     * 
     * @return the reading time of the post in minutes
    */
    public String calulateReadingTime() {
        if (this.content == null) return "0 min";

        int words = this.content.split(" ").length;
        int minutes = words / 200;

        if (minutes < 1) minutes = 1;
        
        String time = String.format("%d min", minutes);
        
        this.readingTime = time;
        return time;
    }

    /**
     * Remove the banner from the post
     */
    public void removeBanner() {
        this.banner = null;
    }

    /**
     * Update the post with the new post data
     * 
     * @param post the new post data to update the post with
     */
    public void updatePost(Post post) {
        this.title = post.getTitle() != null ? post.getTitle() : this.title;
        this.description = post.getDescription() != null ? post.getDescription() : this.description;
        this.content = post.getContent() != null ? post.getContent() : this.content;
        this.author = post.getAuthor() != null ? post.getAuthor() : this.author;
        this.date = post.getDate() != null ? post.getDate() : this.date;
        this.readingTime = post.getReadingTime() != null ? post.getReadingTime() : this.readingTime;
        this.visibility = post.getVisibility() != null ? post.getVisibility() : this.visibility;
        this.tags = post.getTags() != null ? post.getTags() : this.tags;
        this.banner = post.getBanner() != null ? post.getBanner() : this.banner;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
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

    public Blob getBanner() {
        return banner;
    }

    public void setBanner(Blob banner) {
        this.banner = banner;
    }

    public void setBanner(String bannerPath) {
        Resource img = new ClassPathResource(bannerPath);
        try {
            this.banner = BlobProxy.generateProxy(img.getInputStream(), img.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
