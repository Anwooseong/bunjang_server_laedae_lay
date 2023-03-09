package com.example.demo.src.point.model;

import com.example.demo.src.point.dto.DetailPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetPointRes {

    private String totalPoint;
    private List<DetailPoint> detailPoints;

}
