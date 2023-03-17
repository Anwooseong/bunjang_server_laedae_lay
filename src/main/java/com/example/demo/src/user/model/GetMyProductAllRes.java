package com.example.demo.src.user.model;

import com.example.demo.src.user.dto.Collection;
import com.example.demo.src.user.dto.MyProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMyProductAllRes {
    private List<Collection> collections;
    private List<MyProduct> myProducts;
}
