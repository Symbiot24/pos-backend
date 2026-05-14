package com.pos.backend.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class BillRequest {

    private Long shopId;
    private List<BillItemRequest> items;
    private String paymentMethod;
}