package com.example.shoppingmanagement.controller.api;

import com.example.shoppingmanagement.model.OrderItem;
import com.example.shoppingmanagement.model.dto.OrderDto;
import com.example.shoppingmanagement.model.product.ProductType;
import com.example.shoppingmanagement.service.OrderService;
import com.example.shoppingmanagement.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Testcontainers
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderRestController orderRestController;

    @MockBean
    OrderService orderService;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("주문을 정상적으로 받는다.")
    void createOrder() throws Exception {
        List<OrderItem> orderItems = List.of(new OrderItem(UUID.randomUUID(), ProductType.TOPS,
                30000, 3));
        String content = objectMapper.writeValueAsString(new OrderDto("test@gmail.com", "test street 34", "12344", orderItems));

        mockMvc.perform(post("/api/v1/order")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 주문을 정상적으로 조회힌다.")
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andDo(print()); // 통합테스트처럼 service단도 다 수행해서 test 해야하는건지?
    }

}