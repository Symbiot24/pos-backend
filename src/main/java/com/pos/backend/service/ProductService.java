package com.pos.backend.service;

import com.pos.backend.model.Product;
import com.pos.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pos.backend.model.Shop;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsByOwner(Shop owner) {
        return productRepository.findByShop(owner);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, Product updatedProduct, Shop currentShop) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔒 OWNER CHECK
        if (!existing.getShop().getId().equals(currentShop.getId())) {
            throw new RuntimeException("You are not allowed to update this product");
        }

        // ✅ Update only allowed fields
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());

        // ⚠️ Optional: update image only if provided
        if (updatedProduct.getImageUrl() != null) {
            existing.setImageUrl(updatedProduct.getImageUrl());
        }


        return productRepository.save(existing);
    }

    public void deleteProduct(Long id, Shop shop) {
       Product product = productRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Product not found"));

       if (!product.getShop().getId().equals(shop.getId())) {
           throw new RuntimeException("Unauthorized to delete this product");
       }

       productRepository.delete(product);
    }
}