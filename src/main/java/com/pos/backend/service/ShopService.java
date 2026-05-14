package com.pos.backend.service;
import com.pos.backend.model.Shop;
public interface ShopService {
    Shop findByEmail(String email);
    Shop save(Shop shop);
}
