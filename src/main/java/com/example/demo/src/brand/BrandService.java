package com.example.demo.src.brand;

import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {

    private final JwtService jwtService;
    private final BrandDao brandDao;
    private final BrandProvider brandProvider;

}
