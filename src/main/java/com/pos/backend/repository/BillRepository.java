package com.pos.backend.repository;

import com.pos.backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByShopId(Long shopId);

    List<Bill> findByShopIdAndBillTimeBetween(
            Long shopId,
            LocalDateTime start,
            LocalDateTime end
    );
}