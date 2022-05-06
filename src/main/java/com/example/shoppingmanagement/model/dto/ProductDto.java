package com.example.shoppingmanagement.model.dto;

import com.example.shoppingmanagement.model.product.ProductSize;
import com.example.shoppingmanagement.model.product.ProductStatus;
import com.example.shoppingmanagement.model.product.ProductType;

public record ProductDto(ProductType productType,
                         String productName,
                         long price,
                         String description,
                         String brandName,
                         ProductSize size,
                         ProductStatus status) {
}
