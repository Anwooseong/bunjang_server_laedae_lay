package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPaymentReq {
    private int userId;
    private int deliveryAddressId;
    private String deliveryRequest;
    private int commisionPrice;
    private int usedPoint;
    private int finalPrice;
    private String paymentMethod;
    private String email;
    private boolean agreement;
}
