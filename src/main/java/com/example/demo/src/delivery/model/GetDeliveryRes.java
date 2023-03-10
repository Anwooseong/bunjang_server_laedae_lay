package com.example.demo.src.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetDeliveryRes {
    private int deliveryId;
    private int productId;
    private String title;
    private String url;

    public GetDeliveryRes(int deliveryId, int productId, String title) {
        this.deliveryId = deliveryId;
        this.productId = productId;
        this.title = title;
    }
}
