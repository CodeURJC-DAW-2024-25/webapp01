package es.daw01.savex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }
}
