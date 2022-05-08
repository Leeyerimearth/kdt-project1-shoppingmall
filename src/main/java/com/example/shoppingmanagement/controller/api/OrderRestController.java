package com.example.shoppingmanagement.controller.api;

import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.OrderStatus;
import com.example.shoppingmanagement.model.dto.OrderDto;
import com.example.shoppingmanagement.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/api/v1/orders")
    @ResponseBody
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok().body(orders);
    }

    @PutMapping("/api/v1/order/{orderId}")
    @ResponseBody
    public ResponseEntity<Order> changeOrderStatus(@RequestParam OrderStatus orderStatus, @PathVariable String orderId) {
        Order order = orderService.changeOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/api/v1/order/{orderId}")
    @ResponseBody
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(order);
    }

}
