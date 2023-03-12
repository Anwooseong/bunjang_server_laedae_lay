package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.exception.DataException;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewRes {
    private int startRating;
    private String content;
    private String userName;
    private String createdAt;
    private int productId;
    private String productTitle;
    private String isSafePay;
}
