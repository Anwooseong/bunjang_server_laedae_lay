package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSearchProductRes {
    private int productId;
    private String title;
    private int price;
    private String url;
    private String status;

    public GetSearchProductRes(int productId, String title, int price, String status) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.status = status;
    }
}
