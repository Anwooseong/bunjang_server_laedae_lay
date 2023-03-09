package com.example.demo.src.product;

import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final JwtService jwtService;
    private final ProductDao productDao;
    private final ProductProvider productProvider;
}
