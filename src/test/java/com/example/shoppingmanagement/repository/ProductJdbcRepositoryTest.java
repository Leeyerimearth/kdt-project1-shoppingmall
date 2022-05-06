package com.example.shoppingmanagement.repository;

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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class ProductJdbcRepositoryTest {

    @Autowired
    ProductJdbcRepository productJdbcRepository;

    @Test
    @DisplayName("등록된 전체 상품을 가져온다.")
    void findAll() {
        Product product = new Product(UUID.randomUUID(),
                ProductType.TOPS,
                "22SS_강아지티셔츠",
                32000,
                "오버사이즈 강아지 티셔츠 입니다.",
                "강아지마을",
                ProductSize.M,
                ProductStatus.IN_STOCK);
        productJdbcRepository.insert(product);

        List<Product> productList = productJdbcRepository.findAll();
        assertThat(productList).isNotEmpty();
    }

    @Test
    @DisplayName("상품을 insert 한다")
    void insert() {
        Product product = new Product(UUID.randomUUID(),
                ProductType.TOPS,
                "22SS_강아지티셔츠",
                32000,
                "오버사이즈 강아지 티셔츠 입니다.",
                "강아지마을",
                ProductSize.M,
                ProductStatus.IN_STOCK);
        Optional<Product> insert = productJdbcRepository.insert(product);
        assertThat(insert.get()).isSameAs(product);
    }

}