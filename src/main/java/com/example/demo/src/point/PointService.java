package com.example.demo.src.point;

import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {
    private final JwtService jwtService;
    private final PointDao pointDao;
    private final PointProvider pointProvider;
}
