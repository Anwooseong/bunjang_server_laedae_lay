package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductInMajorRes {
    private int productId;
    private String title;
    private String imgUrl;
    private int seller_id;
    private String isPay;
    private String isAd;
    private String isInMyProduct;

    private int price;
    private String isSafeCare;
    private String createdAt;
    private int view;
}
