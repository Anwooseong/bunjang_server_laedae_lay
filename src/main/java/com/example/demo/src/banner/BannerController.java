package com.example.demo.src.banner;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.banner.model.GetBannerRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/home/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;
    private final BannerProvider bannerProvider;
    private final JwtService jwtService;

    /**
     * 배너 이미지 리스트 조회 API
     * [GET] /app/home/banners
     * @return BaseResponse<GetBannerRes>
     */
    @GetMapping("")
    public BaseResponse<GetBannerRes> findBanners() {
        try {
            int userId = jwtService.getUserId();
            bannerProvider.getValidUser(userId);
            GetBannerRes getBannerRes = bannerProvider.getBannersUrl();
            return new BaseResponse<>(getBannerRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
