package com.pos.backend.repository;
import com.pos.backend.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByEmail(String email);

    boolean existsByEmail(String email);

}
