package com.example.demo.src.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MainProductDto {
    private int id;
    private String categoryTitle;
    private String title;
    private String url;
    private int price;
    private boolean isSafePay;
    private boolean isSafeCare;

    public MainProductDto(int id, String categoryTitle, String title, int price, boolean isSafePay, boolean isSafeCare) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.title = title;
        this.price = price;
        this.isSafePay = isSafePay;
        this.isSafeCare = isSafeCare;
    }
}
