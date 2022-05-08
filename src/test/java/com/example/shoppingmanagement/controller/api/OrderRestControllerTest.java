package com.example.shoppingmanagement.controller.api;

import com.example.shoppingmanagement.model.OrderItem;
import com.example.shoppingmanagement.model.OrderStatus;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @Test
    @DisplayName("Id로 주문을 가져온다.")
    void getOrderById() throws Exception {
        String orderId = UUID.randomUUID().toString();
        mockMvc.perform(get("/api/v1/order/{orderId}",orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("status를 정상적으로 변경한다.")
    void changeOrderStatus() throws Exception {
        String orderId = UUID.randomUUID().toString();

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("orderStatus", OrderStatus.CANCELLED.toString());

        mockMvc.perform(put("/api/v1/order/{orderId}",orderId)
                        .params(info))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("enum에 없는 status가 들어왔을경우 매핑에러가 난다.")
    void changeOrderStatusError() throws Exception {
        String orderId = UUID.randomUUID().toString();

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("orderStatus", "unknown");

        mockMvc.perform(put("/api/v1/order/{orderId}",orderId)
                        .params(info))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}