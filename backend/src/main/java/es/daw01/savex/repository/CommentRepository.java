package es.daw01.savex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Iterable<Comment> findByPost(Post post);
    public Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);
}
