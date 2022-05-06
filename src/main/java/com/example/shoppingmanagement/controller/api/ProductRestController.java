package com.example.shoppingmanagement.controller.api;

import com.example.shoppingmanagement.model.dto.ProductDto;
import com.example.shoppingmanagement.model.product.Product;
import com.example.shoppingmanagement.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok().body(productList);
    }

    @PostMapping("/api/v1/product")
    @ResponseBody
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        return ResponseEntity.ok().body(product);
    }
}
