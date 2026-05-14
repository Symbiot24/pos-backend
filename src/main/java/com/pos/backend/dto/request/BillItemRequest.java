package com.pos.backend.dto.request;

import lombok.Data;

@Data
public class BillItemRequest {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}