package com.pos.backend.service.impl;

import com.pos.backend.dto.request.LoginRequest;
import com.pos.backend.dto.request.SignupRequest;
import com.pos.backend.dto.response.JwtResponse;
import com.pos.backend.exception.ResourceNotFoundException;
import com.pos.backend.model.Shop;
import com.pos.backend.repository.ShopRepository;
import com.pos.backend.security.JwtTokenProvider;
import com.pos.backend.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    // ✅ Constructor Injection (clean + reliable)
    public AuthServiceImpl(
            ShopRepository shopRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider
    ) {
        this.shopRepository = shopRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // =========================
    // SIGNUP
    // =========================
    @Override
    @Transactional
    public JwtResponse registerShopOwner(SignupRequest request) {

        // ✅ Check duplicate email
        if (shopRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // ✅ Create new shop
        Shop shop = new Shop();
        shop.setFullName(request.getFullName());
        shop.setShopName(request.getShopName());
        shop.setEmail(request.getEmail());
        shop.setAddress(request.getAddress());

        // 🔥 IMPORTANT: always encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        shop.setPassword(encodedPassword);

        shop.setIsActive(true);

        shop = shopRepository.save(shop);

        // ✅ Generate tokens
        String accessToken = tokenProvider.generateAccessToken(
                shop.getId(), shop.getEmail()
        );

        String refreshToken = tokenProvider.generateRefreshToken(
                shop.getId(), shop.getEmail()
        );

        return new JwtResponse(
                accessToken,
                refreshToken,
                shop.getId(),
                shop.getFullName(),
                shop.getShopName(),
                shop.getEmail()
        );
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public JwtResponse login(LoginRequest request) {

        // ✅ Find shop
        Shop shop = shopRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials")
                );

        boolean isMatch = passwordEncoder.matches(
                request.getPassword(),
                shop.getPassword()
        );

        // ❌ If password mismatch
        if (!isMatch) {
            throw new RuntimeException("Invalid credentials");
        }

        // ❌ If account inactive
        if (!shop.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        // ✅ Generate tokens
        String accessToken = tokenProvider.generateAccessToken(
                shop.getId(), shop.getEmail()
        );

        String refreshToken = tokenProvider.generateRefreshToken(
                shop.getId(), shop.getEmail()
        );

        return new JwtResponse(
                accessToken,
                refreshToken,
                shop.getId(),
                shop.getFullName(),
                shop.getShopName(),
                shop.getEmail()
        );
    }
}
