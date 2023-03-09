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
    private int price;
    private boolean isSafePay;
    private boolean isSafeCare;
}
