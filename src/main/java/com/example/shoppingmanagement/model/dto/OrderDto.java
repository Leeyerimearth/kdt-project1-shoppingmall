package com.example.shoppingmanagement.model.dto;

import com.example.shoppingmanagement.model.OrderItem;

import java.util.List;

public record OrderDto(String email, String address, String postcode, List<OrderItem> orderItems) {
}
