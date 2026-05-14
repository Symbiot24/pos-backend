package com.pos.backend.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos.backend.model.Shop;
import com.pos.backend.repository.ShopRepository;
import com.pos.backend.service.ShopService;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public Shop findByEmail(String email) {
        return shopRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Shop not found : " + email));
    }

    @Override
    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }
}
