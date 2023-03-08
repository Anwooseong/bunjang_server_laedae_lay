package com.example.demo.src.powerad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//id, title, price, seller, imageUrl, url
@Setter
@Getter
@AllArgsConstructor
public class GetPowerAdRes {

    private int id;
    private String title;
    private int price;
    private String seller;
    private String imageUrl;
    private String url;

}
