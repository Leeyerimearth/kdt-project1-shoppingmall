package com.example.shoppingmanagement.service;

import com.example.shoppingmanagement.exception.OrderException;
import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.OrderItem;
import com.example.shoppingmanagement.model.OrderStatus;
import com.example.shoppingmanagement.repository.OrderJdbcRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderJdbcRepository orderJdbcRepository;

    public OrderService(OrderJdbcRepository orderJdbcRepository) {
        this.orderJdbcRepository = orderJdbcRepository;
    }

    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(UUID.randomUUID(), email, address, postcode, orderItems, OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
        Optional<Order> returnOrder = orderJdbcRepository.insert(order);
        returnOrder.orElseThrow(() -> new OrderException("주문이 정상적으로 접수되지 않았습니다."));
        return returnOrder.get();
    }

    public List<Order> getAllOrders() {
        return orderJdbcRepository.findAll();
    }

    public Order changeOrderStatus(String orderId, OrderStatus changeStatus) {
        Optional<Order> updatedOrder = orderJdbcRepository.updateOrderStatus(UUID.fromString(orderId), changeStatus);
        updatedOrder.orElseThrow(() -> new OrderException("상태가 변경되지 않았습니다."));
        return updatedOrder.get();
    }

    public Order getOrderById(String orderId) {
        Optional<Order> order = orderJdbcRepository.findById(UUID.fromString(orderId));
        order.orElseThrow(() -> new OrderException(orderId + " 에 해당하는 order정보가 없습니다."));
        return order.get();
    }
}
