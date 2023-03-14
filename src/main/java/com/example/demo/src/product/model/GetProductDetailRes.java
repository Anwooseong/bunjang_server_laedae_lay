package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductDetailRes {
   private List<String> productImgUrl;
   private int price;
   private String isSafePay;
   private String title;
   private String locationAddress;
   private String createdAt;
   private String dayCreatedFrom;
   private String hourCreatedFrom;
   private int view = 0;
   private int likes = 0;
   private int chatCounts = 0;
   private String hasDeliveryFee;
   private String isNew;
   private int amount;
   private String isInterchangeable;
   private String content;
   private String categoryImgUrl;
   private String categoryTitle;
   private String brandImgUrl;
   private String brandName;
   private List<String> tags;

    public GetProductDetailRes(int price, String isSafePay, String title, String locationAddress, String dayCreatedFrom, String hourCreatedFrom, String createdAt, int view, String hasDeliveryFee, String isNew, int amount, String isInterchangeable, String content, String categoryImg, String categoryTitle, String brandImg, String brandName) {
        this.price = price;
        this.isSafePay = isSafePay;
        this.title = title;
        this.locationAddress = locationAddress;
        this.dayCreatedFrom = dayCreatedFrom;
        this.hourCreatedFrom = hourCreatedFrom;
        this.createdAt = createdAt;
        this.view = view;
        this.hasDeliveryFee = hasDeliveryFee;
        this.isNew = isNew;
        this.amount = amount;
        this.isInterchangeable = isInterchangeable;
        this.content = content;
        this.categoryImgUrl = categoryImg;
        this.categoryTitle = categoryTitle;
        this.brandImgUrl = brandImg;
        this.brandName = brandName;
    }
}
