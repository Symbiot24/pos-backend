package com.pos.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.backend.dto.request.BillItemRequest;
import com.pos.backend.dto.request.BillRequest;
import com.pos.backend.enums.PaymentMethod;
import com.pos.backend.model.Bill;
import com.pos.backend.model.BillItem;
import com.pos.backend.model.Product;
import com.pos.backend.model.Shop;
import com.pos.backend.repository.BillRepository;
import com.pos.backend.repository.ProductRepository;
import com.pos.backend.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Bill createBill(BillRequest request) {

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        List<BillItem> billItems = new ArrayList<>();
        double totalAmount = 0;

        for (BillItemRequest itemReq : request.getItems()) {

            // 🔹 Fetch product from DB (validation)
            Product product = productRepository
                    .findByIdAndShopId(itemReq.getProductId(), request.getShopId())
                    .orElseThrow(() -> new RuntimeException("Invalid product for this shop"));

            // 🔹 Use DB data (not frontend)
            double itemTotal = product.getPrice() * itemReq.getQuantity();

            BillItem item = new BillItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemReq.getQuantity());
            item.setTotal(itemTotal);

            totalAmount += itemTotal;

            billItems.add(item);
        }

        double tax = totalAmount * 0.05; // example 5%
        double discount = 0;
        double finalAmount = totalAmount + tax - discount;

        Bill bill = new Bill();
        bill.setBillTime(LocalDateTime.now());
        bill.setTotalAmount(totalAmount);
        bill.setTaxAmount(tax);
        bill.setDiscount(discount);
        bill.setFinalAmount(finalAmount);
        bill.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        bill.setStatus("COMPLETED");
        bill.setShop(shop);

        // link items to bill
        for (BillItem item : billItems) {
            item.setBill(bill);
            item.setShop(shop);
        }

        bill.setItems(billItems);

        return billRepository.save(bill);
    }

    public List<Bill> getBillsByShop(Long shopId) {
        return billRepository.findByShopId(shopId);
    }
}