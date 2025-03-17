package es.daw01.savex.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;
import jakarta.transaction.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Iterable<Comment> findByPost(Post post);
    public Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);
    public Page<Comment> findByPostIdOrderByCreatedAtDesc(long post_id, Pageable pageable);
    public Optional<Comment> findByPostIdAndId(long post_id, long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.author.id = ?1")
    public void deleteByAuthorId(long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.post.id = ?1")
    public void deleteByPostId(long id);
}
