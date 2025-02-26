package es.daw01.savex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.daw01.savex.model.Post;
import es.daw01.savex.repository.PostRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    /**
     * Saves a post in the database
     * @param post The post to save
    */
    public void save(Post post) {
        postRepository.save(post);
    }

    /**
     * Saves a post in the database
     * @param post The post to save
     * @param banner The banner image of the post
    */
    public void save(Post post, MultipartFile banner){
        try {
            post.saveImage(banner);
            postRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a post from the database
     * @param id The id of the post to delete
    */
    public void deleteById(long id) {
        postRepository.deleteById(id);
    }

    /**
     * Finds all posts in the database
     * @return A list of all posts in the database
    */
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /**
     * Finds a post by its id
     * @param id The id of the post to find
     * @return The post with the given id, or null if it doesn't exist
    */
    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Page<Post> findAllOrderByCreatedAtDesc(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
