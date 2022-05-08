package com.example.shoppingmanagement.controller;

import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.OrderStatus;
import com.example.shoppingmanagement.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @GetMapping("/orders/detail/{orderId}")
    public String getOrderDetail(@PathVariable String orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/orderDetail";
    }

    @GetMapping("/order/{orderId}")
    public String changeOrderStatusForm(@PathVariable String orderId, Model model) {
        List<String> orderStatuses = new ArrayList<>();
        Arrays.stream(OrderStatus.values()).forEach(value -> {
            orderStatuses.add(value.name());
        });
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderStatuses", orderStatuses);
        return "order/changeOrderStatusForm";
    }

    @PostMapping("/order/status")
    public String changeOrderStatus(@RequestParam Map<String, String> parameter, Model model) {
        Order order = orderService.changeOrderStatus(parameter.get("orderId"), OrderStatus.valueOf(parameter.get("changeStatus")));
        model.addAttribute("order",order);
        return "order/orderDetail";
    }

}
