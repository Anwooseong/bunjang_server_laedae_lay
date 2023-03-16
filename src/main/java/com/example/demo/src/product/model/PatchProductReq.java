package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatchProductReq {
    private int userId;
    private String title;
    private String content;
    private int majorCategoryId;
    private int middleCategoryId;

    @Nullable
    private int subCategoryId;
    private List<Integer> tagIds;
    private int price;
    private String hasDeliveryFee;
    private int amount;
    private String checkNewProduct;
    private String checkExchange;
    private String region;
    private String checkPay;
}
