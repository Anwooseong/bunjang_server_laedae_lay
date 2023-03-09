package com.example.demo.src.brand.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetBrandRes {
    private int brandId;
    private String koreanName;
    private String englishName;
    private String imageUrl;
    private int count;
    private boolean follow;
}
