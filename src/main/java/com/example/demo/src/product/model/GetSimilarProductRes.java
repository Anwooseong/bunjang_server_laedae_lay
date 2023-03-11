package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSimilarProductRes {
    private int productId;
    private String title;
    private int price;
    private String imageUrl;
    private boolean checkMyProduct;
    private boolean checkPay;

    public GetSimilarProductRes(int productId, String title, int price, boolean checkPay) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.checkPay = checkPay;
    }
}
