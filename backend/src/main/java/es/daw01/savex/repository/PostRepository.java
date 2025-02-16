package es.daw01.savex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}