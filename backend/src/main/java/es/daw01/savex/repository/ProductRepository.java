package es.daw01.savex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}