package com.example.shoppingmanagement.controller.api;

import com.example.shoppingmanagement.model.dto.ProductDto;
import com.example.shoppingmanagement.model.product.ProductSize;
import com.example.shoppingmanagement.model.product.ProductStatus;
import com.example.shoppingmanagement.model.product.ProductType;
import com.example.shoppingmanagement.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Testcontainers
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRestController productRestController;

    @Test
    @DisplayName("전체 상품리스트를 가져오는 컨트롤러가 정상 호출된다")
    void getAllProduct() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("createProduct 컨트롤러 메서드가 정상 호출 된다")
    void createProduct() throws Exception {
        String content = objectMapper.writeValueAsString(new ProductDto(
                ProductType.valueOf("TOPS"),
                "testProduct",
                3000,
                "it's testproduct",
                "testbrand",
                ProductSize.M,
                ProductStatus.IN_STOCK));

        mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 Dto가 전달되면 GlobalControllerExceptionHandler가 호출된다.")
    void createProductErrorCallExceptionHandler() throws Exception {
        String content = objectMapper.writeValueAsString(new ProductDto(
                ProductType.valueOf("TOPS"),
                "testProduct",
                3000,
                "it's testproduct",
                "testbrand",
                ProductSize.M,
                ProductStatus.IN_STOCK));

        when(productRestController.createProduct(any())).thenThrow(new IllegalArgumentException());

        MvcResult result = mockMvc
                .perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("mapping error");
    }

    @Test
    @DisplayName("view에서 유효하지 않은 Enum값이 전달되면, Dto 매핑중 IllegalArgumentException 오류가 발생한다.")
    void createProductDtoMappingError() {
        String errorMessage = "";
        try {
            objectMapper.writeValueAsString(new ProductDto(
                    ProductType.valueOf("unknown"), //유효하지 않은 enum value
                    "testProduct",
                    3000,
                    "it's testproduct",
                    "testbrand",
                    ProductSize.M,
                    ProductStatus.IN_STOCK));
        } catch (IllegalArgumentException | JsonProcessingException e) {
            errorMessage = e.toString();
        }
        assertThat(errorMessage).contains("IllegalArgumentException");
    }
}