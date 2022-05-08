package com.example.shoppingmanagement.service;

import com.example.shoppingmanagement.exception.OrderException;
import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.OrderItem;
import com.example.shoppingmanagement.model.OrderStatus;
import com.example.shoppingmanagement.model.dto.ProductDto;
import com.example.shoppingmanagement.model.product.Product;
import com.example.shoppingmanagement.model.product.ProductSize;
import com.example.shoppingmanagement.model.product.ProductStatus;
import com.example.shoppingmanagement.model.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("주문을 정상적으로 생성한다.")
    void createOrder() {
        ProductDto productDto = new ProductDto(ProductType.TOPS, "test", 30000,
                "this is test product", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);

        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), product.getProductType(),
                product.getPrice(), 3));

        Order order = orderService.createOrder(new Email("test@gmail.com"), "test street 23", "12345", orderItems);

        assertThat(order.getOrderItems().get(0).price()).isEqualTo(productDto.price());
    }

    @Test
    @DisplayName("address 혹은 postcode가 nullstring이면 주문예외가 발생한다.")
    void createOrderException() {
        ProductDto productDto = new ProductDto(ProductType.TOPS, "test", 30000,
                "this is test product", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);

        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), product.getProductType(),
                product.getPrice(), 3));

        assertThrows(OrderException.class, () ->
                orderService.createOrder(new Email("test@gmail.com"), "    ", "12345", orderItems));
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void getAllOrders() {
        ProductDto productDto = new ProductDto(ProductType.TOPS, "test", 30000,
                "this is test product", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);

        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), product.getProductType(),
                product.getPrice(), 3));

        Order order = orderService.createOrder(new Email("test@gmail.com"), "test street 23", "12345", orderItems);

        List<Order> orders = orderService.getAllOrders();

        assertThat(orders.size()).isGreaterThan(1);
        assertThat(orders.stream().filter(o -> o.getOrderId().equals(order.getOrderId())).findAny()).isNotEmpty();
    }

    @Test
    @DisplayName("orderId로 주문을 가져온다.")
    void getOrderById() {
        ProductDto productDto = new ProductDto(ProductType.TOPS, "test", 30000,
                "this is test product", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);

        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), product.getProductType(),
                product.getPrice(), 3));

        Order order = orderService.createOrder(new Email("test@gmail.com"), "test street 23", "12345", orderItems);

        Order returnOrder = orderService.getOrderById(order.getOrderId().toString());
        assertThat(returnOrder.getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    @DisplayName("없는 orderId를 가져오면 exception이 발생한다.")
    void getOrderByIdOccurException() {
        assertThrows(OrderException.class, () -> orderService.getOrderById(UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName("orderStatus를 정상적으로 변경한다.")
    void changeOrderStatus() {
        ProductDto productDto = new ProductDto(ProductType.TOPS, "test", 30000,
                "this is test product", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), product.getProductType(),
                product.getPrice(), 3));
        Order order = orderService.createOrder(new Email("test@gmail.com"), "test street 23", "12345", orderItems);

        Order returnOrder = orderService.changeOrderStatus(order.getOrderId().toString(), OrderStatus.CANCELLED);

        assertThat(returnOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }
}