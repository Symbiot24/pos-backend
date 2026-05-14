package com.pos.backend.controller;

import com.pos.backend.dto.request.BillRequest;
import com.pos.backend.model.Bill;
import com.pos.backend.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@CrossOrigin
public class BillController {

    private final BillService billService;

    // ✅ Generate Bill
    @PostMapping
    public Bill createBill(@RequestBody BillRequest request) {
        return billService.createBill(request);
    }

    // ✅ Get bills for a shop
    @GetMapping("/shop/{shopId}")
    public List<Bill> getBills(@PathVariable Long shopId) {
        return billService.getBillsByShop(shopId);
    }
}