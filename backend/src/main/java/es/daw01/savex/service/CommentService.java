package es.daw01.savex.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.daw01.savex.DTOs.CommentDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.comments.CommentMapper;
import es.daw01.savex.DTOs.comments.CreateCommentRequest;
import es.daw01.savex.DTOs.comments.SimpleCommentDTO;
import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * Get all comments from the database paginated
     * @param pageable Pageable object to paginate the results
     * @return Page of comments
     */
    public PaginatedDTO<SimpleCommentDTO> retrieveComments(long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        List<SimpleCommentDTO> commentDTOs = commentMapper.toDTOSimple(comments.getContent());
            
        return new PaginatedDTO<>(
            commentDTOs,
            comments.getNumber(),
            comments.getTotalPages(),
            comments.getTotalElements(),
            comments.getSize(),
            comments.isLast()
        );
    }

    /**
     * Create a comment in the database
     * @param postId Post id
     * @param request Comment request
     * @param author Comment author
     * @return Comment created
    */
    public SimpleCommentDTO createComment(long postId, CreateCommentRequest request, User author) {
        Post post = postService.findById(postId).orElseThrow();
        Comment comment = commentMapper.toDomain(request, author, post);
        return commentMapper.toDTOSimple(commentRepository.save(comment));
    }

    /**
     * Delete a comment from the database
     * @param postId Post id
     * @param commentId Comment id
     * @param author Comment author
     * @return Comment deleted
    */
    public SimpleCommentDTO deleteComment(long postId, long commentId, User author) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.isAuthor(author)) {
            commentRepository.delete(comment);
            return commentMapper.toDTOSimple(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the author of this comment");
        }
    }

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

    /**
     * Delete all comments by a given author
     * @param authorId Author id
     * @return Number of deleted comments
     */
    public void deleteByAuthorId(Long authorId) {
        commentRepository.deleteByAuthorId(authorId);
    }

    /**
     * Delete all comments from a post
     * @param postId Post id
     * @return Number of deleted comments
     */
    public void deleteByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }
}
