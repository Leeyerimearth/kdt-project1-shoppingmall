package com.example.shoppingmanagement.repository;

import com.example.shoppingmanagement.model.product.Product;
import com.example.shoppingmanagement.model.product.ProductSize;
import com.example.shoppingmanagement.model.product.ProductStatus;
import com.example.shoppingmanagement.model.product.ProductType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.*;

@Repository
public class ProductJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    public Optional<Product> insert(Product product) {
        Map<String, Object> paramMap = new HashMap<>() {{
            put("productId", product.getProductId().toString().getBytes());
            put("productType", product.getProductType().name());
            put("productName", product.getProductName());
            put("description", product.getDescription());
            put("price", product.getPrice());
            put("brandName", product.getBrandName());
            put("size", product.getSize().name());
            put("status", product.getStatus().name());
        }};
        int result = jdbcTemplate.update("INSERT INTO products (product_id, product_type, product_name, description, price, brand_name, size, status) " +
                "VALUES (UUID_TO_BIN(:productId), :productType, :productName, :description, :price, :brandName, :size, :status)", paramMap);

        if (result == 1) {
            return Optional.of(product);
        }
        return Optional.empty();
    }

    private final RowMapper<Product> productRowMapper = (resultSet, rowNum) -> {
        UUID productId = toUUID(resultSet.getBytes("product_id"));
        ProductType productType = ProductType.valueOf(resultSet.getString("product_type"));
        String productName = resultSet.getString("product_name");
        long price = resultSet.getLong("price");
        String description = resultSet.getString("description");
        String brandName = resultSet.getString("brand_name");
        ProductSize size = ProductSize.valueOf(resultSet.getString("size"));
        ProductStatus status = ProductStatus.valueOf(resultSet.getString("status"));
        return new Product(productId, productType, productName, price, description, brandName, size, status);
    };

    private UUID toUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long firstLong = byteBuffer.getLong();
        long secondLong = byteBuffer.getLong();
        return new UUID(firstLong, secondLong);

    }

}
