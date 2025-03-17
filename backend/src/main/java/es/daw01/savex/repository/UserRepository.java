package es.daw01.savex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;


import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAllByRole(UserType role);
    Page<User> findAllByRole(UserType role, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}
