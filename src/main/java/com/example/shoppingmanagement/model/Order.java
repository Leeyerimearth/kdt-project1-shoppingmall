package com.example.shoppingmanagement.model;

import com.example.shoppingmanagement.exception.OrderException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final Email email;
    private String address;
    private String postcode;
    private final List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID orderId, Email email, String address, String postcode, List<OrderItem> orderItems, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validateCreateProduct();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Email getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void validateCreateProduct() {
        if (nullString(address) || nullString(postcode)) {
            throw new OrderException("invalid Order input");
        }
    }

    private boolean nullString(String inputString) {
        return inputString.replace(" ", "").equals("");
    }
}
