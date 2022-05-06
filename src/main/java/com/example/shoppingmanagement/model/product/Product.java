package com.example.shoppingmanagement.model.product;

import com.example.shoppingmanagement.exception.ProductException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Product {
    private final UUID productId;
    private ProductType productType;
    private String productName;
    private long price;
    private String description;
    private String brandName;
    private ProductSize size;
    private ProductStatus status;

    public Product(UUID productId, ProductType productType, String productName, long price, String description,
                   String brandName, ProductSize size, ProductStatus status) {
        this.productId = productId;
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.brandName = brandName;
        this.size = size;
        this.status = status;
        validateCreateProduct();
    }

    private void validateCreateProduct() {
        if (productType == null) {
            throw new ProductException("invalid input");
        }
        if (size == null) {
            throw new ProductException("invalid input");
        }
        if (status == null) {
            throw new ProductException("invalid input");
        }
        if (nullString(productName) || nullString(brandName)) {
            throw new ProductException("invalid input");
        }
    }

    private boolean nullString(String inputString) {
        return inputString.equals("");
    }

}
