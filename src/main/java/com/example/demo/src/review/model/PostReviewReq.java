package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewReq {
    private int buyerId;
    private int sellerId;
    private int productId;
    private int starRating;
    private String content;
}
