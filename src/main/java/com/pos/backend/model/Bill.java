package com.pos.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.backend.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime billTime;

    private Double totalAmount;
    private Double taxAmount;
    private Double discount;
    private Double finalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String status;

    // 🔑 Multi-tenant mapping
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BillItem> items;
}