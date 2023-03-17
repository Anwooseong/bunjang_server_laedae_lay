package com.example.demo.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Collection {
    private int id;
    private String name;
    private int count;
    private String imgUrl;
}
