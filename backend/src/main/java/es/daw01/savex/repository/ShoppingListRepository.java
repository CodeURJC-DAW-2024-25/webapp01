package es.daw01.savex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.ShoppingList;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    
}
