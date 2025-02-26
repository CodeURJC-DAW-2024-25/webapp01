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

import es.daw01.savex.model.Comment;
import es.daw01.savex.model.CommentDTO;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Save a comment in the database
     * @param comment Comment to save
    */
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    /**
     * Save a comment in the database with the given content, author and post
     * @param content Comment content
     * @param author Comment author
     * @param post Comment post
    */
    public void save(String content, User author, Post post) {
        Comment comment = new Comment(author, post, content);
        commentRepository.save(comment);
    }

    /**
     * Delete a comment from the database
     * @param id Comment id to be deleted
    */
    public void deleteById(Long id) {
        Optional<Comment> op = commentRepository.findById(id);

        // If the comment exists, delete it
        if (op.isPresent()) {
            Comment comment = op.get();
            comment.removeFromPost();
            comment.removeFromAuthor();
            commentRepository.delete(comment);
        }
    }

    /**
     * Edit a comment in the database
     * @param id Comment id to be edited
    */
    public void editById(Long id, String content) {
        Optional<Comment> op = commentRepository.findById(id);

        // If the comment exists, edit it
        if (op.isPresent()) {
            Comment comment = op.get();
            comment.setContent(content);
            commentRepository.save(comment);
        }
    }

    /**
     * Get a comment from the database by its id
     * @param id Comment id
     * @return Comment with the given id or null if it doesn't exist
    */
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * Get all comments from a post
     * @param post Post to get the comments from
     * @return List of comments from the given post
    */
    public Iterable<Comment> findByPost(Post post) {
        return commentRepository.findByPost(post);
    }

    /**
     * Get all comments from a post paginated
     * @param post Post to get the comments from
     * @param pageable Pageable object to paginate the results
     * @return Page of comments from the given post
    */
    public Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable);
    }

    /**
     * Get all comments from a post as DTOs
     * @param post Post to get the comments from
     * @param user Current authenticates user
     * @return List of comments from the given post as DTOs
    */
    public List<CommentDTO> getCommentsDTO(Post post, User user) {
        List<CommentDTO> commentsDTO = new ArrayList<>();

        // Get all comments from the post and convert them to DTOs
        for (Comment comment : post.getComments()) {
            commentsDTO.add(new CommentDTO(comment, user));
        }

        return commentsDTO;
    }

    /**
     * Parses all comments to DTO format
     * 
     * @param comments Comments to be parsed
     * @param user Current authenticates user
     * @return List of comments as DTOs
    */
    public List<CommentDTO> getCommentsDTO(List<Comment> comments, User user) {
        List<CommentDTO> commentsDTO = new ArrayList<>();

        // Parse every comment to a DTO format
        for (Comment comment : comments) {
            commentsDTO.add(new CommentDTO(comment, user));
        }

        return commentsDTO;
    }

    public Map<String, Object> retrieveCommentsFromPost(Post post, User user, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve comments of the post paginated
        Page<Comment> commentPage = this.findByPostOrderByCreatedAtDesc(post, pageable);

        // Create comment DTO list
        List<CommentDTO> commentDTOs = this.getCommentsDTO(commentPage.getContent(), user);

        // Generate response map
        response.put("comments", commentDTOs);
        response.put("currentPage", commentPage.getNumber());
        response.put("totalItems", commentPage.getTotalElements());
        response.put("totalPages", commentPage.getTotalPages());
        response.put("isLastPage", commentPage.isLast());

        return response;
    }
}
