package com.pos.backend.controller;

import com.pos.backend.model.Product;
import com.pos.backend.model.Shop;
import com.pos.backend.service.ProductService;
import com.pos.backend.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private Cloudinary cloudinary;

    // ✅ CREATE PRODUCT WITH OWNER
    @PostMapping("/upload")
    public Product createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication
    ) throws IOException {

        // 🔥 GET LOGGED-IN SHOP OWNER
        String email = authentication.getName();
        Shop owner = shopService.findByEmail(email); // ✅ using your ShopService

        String imagePath = null;

        if (image != null && !image.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.emptyMap()
            );

            imagePath = uploadResult.get("secure_url").toString();
        }
        // Create product
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imagePath);
        product.setShop(owner);

        return productService.createProduct(product);
    }

    // ✅ GET ONLY MY PRODUCTS
    @GetMapping
    public List<Product> getMyProducts(Authentication authentication) {

        String email = authentication.getName();
        Shop owner = shopService.findByEmail(email);

        return productService.getProductsByOwner(owner);
    }

    // ✅ GET BY ID (can improve later with ownership check)
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // ✅ UPDATE (basic, not secured yet)
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public Product update(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "removeImage", required = false) Boolean removeImage,
            Authentication authentication
    ) throws IOException {

        // 🔐 Get logged-in user
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email);

        // 🔍 Get existing product
        Product existing = productService.getProductById(id);

        // 🔒 OWNER CHECK
        if (!existing.getShop().getId().equals(shop.getId())) {
            throw new RuntimeException("You are not allowed to update this product");
        }

        // ✅ Update basic fields
        existing.setName(name);
        existing.setDescription(description);
        existing.setPrice(price);

        // ✅ REMOVE IMAGE (if requested)
        if (Boolean.TRUE.equals(removeImage)) {
            existing.setImageUrl(null);
        }

        // ✅ UPDATE IMAGE (Cloudinary)
        if (image != null && !image.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.emptyMap()
            );

            existing.setImageUrl(uploadResult.get("secure_url").toString());
        }

        return productService.createProduct(existing); // save
    }

    // ⚠️ DELETE (NOT SECURE YET)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Shop shop = shopService.findByEmail(email);

        productService.deleteProduct(id, shop);
        return "Product deleted Successfully";
    }
}
