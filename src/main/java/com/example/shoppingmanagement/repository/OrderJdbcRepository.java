package com.example.shoppingmanagement.repository;

import com.example.shoppingmanagement.model.Email;
import com.example.shoppingmanagement.model.Order;
import com.example.shoppingmanagement.model.OrderItem;
import com.example.shoppingmanagement.model.OrderStatus;
import com.example.shoppingmanagement.model.dto.OrderItemMapper;
import com.example.shoppingmanagement.model.dto.OrderMapper;
import com.example.shoppingmanagement.model.product.ProductType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class OrderJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Optional<Order> insert(Order order) {
        int result = jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)", toOrderParamMap(order));

        order.getOrderItems()
                .forEach(item -> {
                    jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, product_type, price, quantity, created_at, updated_at) " +
                                    "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :productType, :price, :quantity, :createdAt, :updatedAt) ",
                            toOrderTimeParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item));
                });
        if (result != 1) {
            return Optional.empty();
        }
        return Optional.of(order);
    }

    @Transactional
    public List<Order> findAll() {
        List<OrderMapper> orderMappers = jdbcTemplate.query("SELECT * FROM orders ", orderRowMapper);
        List<OrderItemMapper> orderItemMappers = jdbcTemplate.query("SELECT * FROM order_items", orderItemRowMapper);

        List<Order> orders = new ArrayList<>();
        orderMappers.forEach(orderMapper -> {
            List<OrderItem> orderItems = new ArrayList<>();
            orderItemMappers.forEach(orderItemMapper -> {
                if (orderItemMapper.orderId().equals(orderMapper.orderId())) {
                    OrderItem orderItem = new OrderItem(orderItemMapper.productId(), orderItemMapper.productType(),
                            orderItemMapper.price(), orderItemMapper.quantity());
                    orderItems.add(orderItem);
                }
            });
            var order = new Order(orderMapper.orderId(), orderMapper.email(), orderMapper.address(), orderMapper.postcode(),
                    orderItems, orderMapper.orderStatus(), orderMapper.createdAt(), orderMapper.updatedAt());
            orders.add(order);
        });

        return orders;
    }

    private RowMapper<OrderMapper> orderRowMapper = (resultSet, rowNum) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new OrderMapper(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);
    };

    private RowMapper<OrderItemMapper> orderItemRowMapper = (resultSet, rowNum) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productType = ProductType.valueOf(resultSet.getString("product_type"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");

        return new OrderItemMapper(orderId, productId, productType, price, quantity);
    };

    private UUID toUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long firstLong = byteBuffer.getLong();
        long secondLong = byteBuffer.getLong();
        return new UUID(firstLong, secondLong);
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderTimeParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("productType", item.productType().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }
}
