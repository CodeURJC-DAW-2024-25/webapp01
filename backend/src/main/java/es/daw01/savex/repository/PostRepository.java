package es.daw01.savex.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.Post;
import es.daw01.savex.model.VisibilityType;


public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    boolean existsById(Long id);
    boolean existsByTitle(String title);
    Page<Post> findByVisibilityOrderByCreatedAtDesc(VisibilityType visibility, Pageable pageable);
}