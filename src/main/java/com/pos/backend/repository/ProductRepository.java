package com.pos.backend.repository;
import com.pos.backend.model.Product;
import com.pos.backend.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShop(Shop shop);

    Optional<Product> findByIdAndShopId(Long product_id, Long shop_id);
}