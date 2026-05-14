package com.pos.backend.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pos.backend.dto.response.ShopProfileResponse;
import com.pos.backend.model.Shop;
import com.pos.backend.service.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final Cloudinary cloudinary;

    @GetMapping("/profile")
    public ShopProfileResponse getProfile(Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email);
        return mapToResponse(shop);
    }

    @PutMapping(value = "/profile", consumes = "multipart/form-data")
    public ShopProfileResponse updateProfile(
            @RequestParam(value = "shopName", required = false) String shopName,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "gstNumber", required = false) String gstNumber,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "supportEmail", required = false) String supportEmail,
            @RequestParam(value = "footerMessage", required = false) String footerMessage,
            @RequestParam(value = "printLogo", required = false) Boolean printLogo,
            @RequestParam(value = "showGst", required = false) Boolean showGst,
            @RequestParam(value = "removeLogo", required = false) Boolean removeLogo,
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            Authentication authentication
    ) throws IOException {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email);

        if (shopName != null) {
            shop.setShopName(shopName.trim());
        }

        if (address != null) {
            shop.setAddress(address.trim());
        }

        if (gstNumber != null) {
            shop.setGstNumber(gstNumber.trim());
        }

        if (phone != null) {
            shop.setPhone(phone.trim());
        }

        if (supportEmail != null) {
            shop.setSupportEmail(supportEmail.trim());
        }

        if (footerMessage != null) {
            shop.setFooterMessage(footerMessage.trim());
        }

        if (printLogo != null) {
            shop.setPrintLogo(printLogo);
        }

        if (showGst != null) {
            shop.setShowGst(showGst);
        }

        if (Boolean.TRUE.equals(removeLogo)) {
            shop.setLogoUrl(null);
        }

        if (logo != null && !logo.isEmpty()) {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    logo.getBytes(),
                    ObjectUtils.emptyMap()
            );
            shop.setLogoUrl(uploadResult.get("secure_url").toString());
        }

        Shop saved = shopService.save(shop);
        return mapToResponse(saved);
    }

    private ShopProfileResponse mapToResponse(Shop shop) {
        return new ShopProfileResponse(
                shop.getId(),
                shop.getShopName(),
                shop.getEmail(),
                shop.getAddress(),
                shop.getGstNumber(),
                shop.getPhone(),
                shop.getSupportEmail(),
                shop.getLogoUrl(),
                shop.getFooterMessage(),
                shop.getPrintLogo(),
                shop.getShowGst()
        );
    }
}
