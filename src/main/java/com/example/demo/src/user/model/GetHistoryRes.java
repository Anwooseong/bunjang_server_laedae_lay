package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetHistoryRes {
    private int productId;
    private String title;
    private String url;
    private String status;
    private String pay;

    public GetHistoryRes(int productId, String title, String status, String pay) {
        this.productId = productId;
        this.title = title;
        this.status = status;
        this.pay = pay;
    }
}
