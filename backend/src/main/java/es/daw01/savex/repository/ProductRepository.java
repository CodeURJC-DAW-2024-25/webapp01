package es.daw01.savex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.daw01.savex.model.Product;
import es.daw01.savex.model.SupermarketType;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySupermarketAndProductId(SupermarketType supermarket, String productId);
}