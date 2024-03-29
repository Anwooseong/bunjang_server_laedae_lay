package com.example.demo.src.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MainProductDto {
    private int id;
    private int userId;
    private String categoryTitle;
    private String title;
    private String url;
    private int price;
    private boolean isSafePay;
    private boolean isSafeCare;
    private boolean checkMyProduct;

    public MainProductDto(int id, int userId, String categoryTitle, String title, int price, boolean isSafePay, boolean isSafeCare) {
        this.id = id;
        this.userId = userId;
        this.categoryTitle = categoryTitle;
        this.title = title;
        this.price = price;
        this.isSafePay = isSafePay;
        this.isSafeCare = isSafeCare;
    }
}
