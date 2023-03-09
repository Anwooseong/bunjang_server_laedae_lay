package com.example.demo.src.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DetailPoint {
    private int id;
    private String  point;
    private String status;
    private String createAt;
}
