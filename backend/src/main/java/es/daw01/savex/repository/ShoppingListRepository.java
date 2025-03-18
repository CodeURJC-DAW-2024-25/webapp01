package es.daw01.savex.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    List<ShoppingList> findAllByUser(User user);
    Page<ShoppingList> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    void deleteAllByUserId(long userId);
}
