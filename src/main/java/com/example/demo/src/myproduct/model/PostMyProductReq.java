package com.example.demo.src.myproduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostMyProductReq {
    private int userId;
    private int productId;
    private Integer collectionId = null;

    public PostMyProductReq(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
