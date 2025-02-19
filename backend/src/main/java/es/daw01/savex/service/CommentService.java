package es.daw01.savex.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.daw01.savex.model.Comment;
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
        commentRepository.deleteById(id);
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
}
