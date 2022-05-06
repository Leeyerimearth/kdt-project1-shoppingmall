package com.example.shoppingmanagement.service;

import com.example.shoppingmanagement.exception.ProductException;
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

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("등록된 전체 상품을 가져온다.")
    void getAllProducts() {
        ProductDto productDto = new ProductDto(ProductType.TOPS,
                "22SS_강아지티셔츠",
                32000,
                "오버사이즈 강아지 티셔츠 입니다.",
                "강아지마을",
                ProductSize.M,
                ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);
        List<Product> allProducts = productService.getAllProducts();
        assertThat(allProducts).isNotEmpty();
    }

    @Test
    @DisplayName("상품을 정상 등록한다.")
    void createProduct() {
        ProductDto productDto = new ProductDto(ProductType.TOPS,
                "22SS_강아지티셔츠",
                32000,
                "오버사이즈 강아지 티셔츠 입니다.",
                "강아지마을",
                ProductSize.M,
                ProductStatus.IN_STOCK);
        Product product = productService.createProduct(productDto);
        assertThat(product.getProductName()).isEqualTo(productDto.productName());
    }

    @Test
    @DisplayName("Enum type이 유효하지 않은 값은 상품을 등록할 수 없다.")
    void createProductEnumError() {
        ProductDto productDto = new ProductDto(null,
                "22SS_강아지티셔츠",
                32000,
                "오버사이즈 강아지 티셔츠 입니다.",
                "강아지마을",
                ProductSize.M,
                ProductStatus.IN_STOCK);
        assertThrows(ProductException.class, () -> productService.createProduct(productDto));
    }

    @Test
    @DisplayName("nullString인 값은 상품을 등록할 수 없다.")
    void createProductError() {
        ProductDto productDto = new ProductDto(ProductType.TOPS,
                "",
                32000,
                "오버사이즈 강아지 티셔츠 입니다.",
                "강아지마을",
                ProductSize.M,
                ProductStatus.IN_STOCK);
        assertThrows(ProductException.class, () -> productService.createProduct(productDto));
    }
}