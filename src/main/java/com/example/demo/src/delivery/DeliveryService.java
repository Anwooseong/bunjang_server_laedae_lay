package com.example.demo.src.delivery;


import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final JwtService jwtService;
    private final DeliveryDao deliveryDao;
    private final DeliveryProvider deliveryProvider;
}
