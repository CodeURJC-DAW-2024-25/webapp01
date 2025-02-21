package es.daw01.savex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
