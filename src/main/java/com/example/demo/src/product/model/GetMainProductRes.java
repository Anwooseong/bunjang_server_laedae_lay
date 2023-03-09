package com.example.demo.src.product.model;

import com.example.demo.src.product.dto.MainProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetMainProductRes {
    List<MainProductDto> manPadding;
    List<MainProductDto> womenPadding;
    List<MainProductDto> manShoes;
    List<MainProductDto> womenShoes;
    List<MainProductDto> sneakers;
}
