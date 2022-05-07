package com.example.shoppingmanagement.model;

import com.example.shoppingmanagement.model.product.ProductType;

import java.util.UUID;

public record OrderItem(UUID productId, ProductType productType, long price, int quantity) {
}
