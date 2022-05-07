package com.example.shoppingmanagement.model.dto;

import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderMapper(UUID orderId,
                          Email email,
                          String address,
                          String postcode,
                          OrderStatus orderStatus,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
}
