package es.daw01.savex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.daw01.savex.model.Post;
import es.daw01.savex.model.PostDTO;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    /**
     * Saves a post in the database
     * 
     * @param post The post to save
     */
    public void save(Post post) {
        postRepository.save(post);
    }

    /**
     * Saves a post in the database
     * 
     * @param post   The post to save
     * @param banner The banner image of the post
     */
    public void save(Post post, MultipartFile banner) {
        try {
            post.saveImage(banner);
            postRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a post from the database
     * 
     * @param id The id of the post to delete
     */
    public void deleteById(long id) {
        postRepository.deleteById(id);
    }

    /**
     * Finds all posts in the database
     * 
     * @return A list of all posts in the database
     */
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /**
     * Finds a post by its id
     * 
     * @param id The id of the post to find
     * @return The post with the given id, or null if it doesn't exist
     */
    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    /**
     * Finds all posts in the database, ordered by creation date and filtered by
     * visibility
     * 
     * @param visibility The visibility of the posts to return
     * @param pageable   The page to return
     * @return A page of posts ordered by creation date
     */
    public Page<Post> findByVisibilityOrderByCreatedAtDesc(VisibilityType visibility, Pageable pageable) {
        return postRepository.findByVisibilityOrderByCreatedAtDesc(visibility, pageable);
    }

    /**
     * Parses a list of posts to a list of post DTOs
     * 
     * @param posts The list of posts to parse
     * @return A list of post DTOs
     */
    public List<PostDTO> getPostsDTO(List<Post> posts) {
        List<PostDTO> postDTOList = new ArrayList<>();

        // Parse each post to a post DTO
        for (Post post : posts) {
            postDTOList.add(new PostDTO(post));
        }

        return postDTOList;
    }

    /**
     * Finds all posts in the database, ordered by creation date
     * 
     * @param pageable The page to return
     * @return A map with the response data
     */
    public Map<String, Object> retrievePosts(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve posts paginated
        Page<Post> postPage = this.findByVisibilityOrderByCreatedAtDesc(VisibilityType.PUBLIC, pageable);

        // Create post DTO list
        List<PostDTO> postDTOList = this.getPostsDTO(postPage.getContent());

        // Generate response map
        response.put("posts", postDTOList);
        response.put("currentPage", postPage.getNumber());
        response.put("totalItems", postPage.getTotalElements());
        response.put("totalPages", postPage.getTotalPages());
        response.put("isLastPage", postPage.isLast());

        return response;
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
