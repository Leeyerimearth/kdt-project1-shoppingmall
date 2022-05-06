package com.example.shoppingmanagement.service;

import com.example.shoppingmanagement.exception.ProductException;
import com.example.shoppingmanagement.model.dto.ProductDto;
import com.example.shoppingmanagement.model.product.Product;
import com.example.shoppingmanagement.repository.ProductJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductJdbcRepository productJdbcRepository;

    public ProductService(ProductJdbcRepository productJdbcRepository) {
        this.productJdbcRepository = productJdbcRepository;
    }

    public List<Product> getAllProducts() {
        return productJdbcRepository.findAll();
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product(UUID.randomUUID(),
                productDto.productType(),
                productDto.productName(),
                productDto.price(),
                productDto.description(),
                productDto.brandName(),
                productDto.size(),
                productDto.status());
        Optional<Product> returnProduct = productJdbcRepository.insert(product);
        returnProduct.orElseThrow(() -> new ProductException("상품이 등록되지 않았습니다."));
        return returnProduct.get();
    }
}
