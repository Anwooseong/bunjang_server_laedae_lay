package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductInRowRes {
    private int productId;
    private String title;
    private String imgUrl;
    private String isPay;
    private String content;
    private String locationAddress;
    private int seller_id;
    private int price;
    private int dayCreatedFrom;
    private int hourCreatedFrom;
    private String createdAt;
}
