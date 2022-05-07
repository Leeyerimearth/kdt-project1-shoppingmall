package com.example.shoppingmanagement.model.dto;

import com.example.shoppingmanagement.model.product.ProductType;

import java.util.UUID;

public record OrderItemMapper(UUID orderId, UUID productId, ProductType productType, long price, int quantity) {
}
