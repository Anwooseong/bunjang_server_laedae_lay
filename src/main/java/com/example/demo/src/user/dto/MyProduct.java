package com.example.demo.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyProduct {
    private int productId;
    private String imgUrl;
    private int sellerId;
    private String isPay;
    private String title;
    private int price;
    private String profileUrl;
    private String userName;
    private String createdAt;
}
