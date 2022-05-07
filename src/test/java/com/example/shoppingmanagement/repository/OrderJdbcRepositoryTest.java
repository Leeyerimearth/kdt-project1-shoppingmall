package com.example.shoppingmanagement.repository;

import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.OrderItem;
import com.example.shoppingmanagement.model.OrderStatus;
import com.example.shoppingmanagement.model.product.Product;
import com.example.shoppingmanagement.model.product.ProductSize;
import com.example.shoppingmanagement.model.product.ProductStatus;
import com.example.shoppingmanagement.model.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class OrderJdbcRepositoryTest {

    @Autowired
    OrderJdbcRepository orderJdbcRepository;

    @Autowired
    ProductJdbcRepository productJdbcRepository;

    @Test
    @DisplayName("주문을 insert한다.")
    void insert() {
        Product product
                = new Product(UUID.randomUUID(), ProductType.TOPS, "test", 30000,
                "this is test Item", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product resultProduct = productJdbcRepository.insert(product).get();

        List<OrderItem> orderItems = List.of(new OrderItem(resultProduct.getProductId(), resultProduct.getProductType(),
                resultProduct.getPrice(), 3));

        Order order = new Order(UUID.randomUUID(), new Email("test@gmail.com"), "test street 23",
                "12344", orderItems, OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
        Optional<Order> resultOrder = orderJdbcRepository.insert(order);
        assertThat(resultOrder.get()).isSameAs(order);
    }

    @Test
    @DisplayName("전체 주문을 가져온다.")
    void findAll() {
        Product product
                = new Product(UUID.randomUUID(), ProductType.TOPS, "test", 30000,
                "this is test Item", "testiee", ProductSize.M, ProductStatus.IN_STOCK);
        Product resultProduct = productJdbcRepository.insert(product).get();

        List<OrderItem> orderItems = List.of(new OrderItem(resultProduct.getProductId(), resultProduct.getProductType(),
                resultProduct.getPrice(), 3), new OrderItem(resultProduct.getProductId(), resultProduct.getProductType(),
                resultProduct.getPrice(), 4)) ;

        Order order = new Order(UUID.randomUUID(), new Email("test@gmail.com"), "test street 23",
                "12344", orderItems, OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
        orderJdbcRepository.insert(order);

        List<Order> orders = orderJdbcRepository.findAll();

        assertThat(orders).size().isGreaterThan(1);
        assertThat(orders.stream().filter(o -> o.getOrderId().equals(order.getOrderId())).findAny()).isNotEmpty();
    }
}