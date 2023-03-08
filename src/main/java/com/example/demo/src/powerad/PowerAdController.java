package com.example.demo.src.powerad;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.powerad.model.GetPowerAdRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/power-ad")
@RequiredArgsConstructor
public class PowerAdController {

    private final JwtService jwtService;
    private final PowerAdProvider powerAdProvider;
    private final PowerAdService powerAdService;

    @GetMapping("")
    public BaseResponse<List<GetPowerAdRes>> getPowerAd() {
        try {
            int userId = jwtService.getUserId();
            powerAdProvider.getValidUser(userId);
            List<GetPowerAdRes> getPowerAdRes = powerAdProvider.getPowerAd();
            return new BaseResponse<>(getPowerAdRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
