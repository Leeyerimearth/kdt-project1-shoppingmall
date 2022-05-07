package com.example.shoppingmanagement.controller.api;

import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.dto.OrderDto;
import com.example.shoppingmanagement.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/order")
    @ResponseBody
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderService.createOrder(new Email(orderDto.email()), orderDto.address(), orderDto.postcode(), orderDto.orderItems());
        return ResponseEntity.ok().body(order);
    }
}
