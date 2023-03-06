package com.example.demo.src.banner;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.banner.model.GetBannerRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerProvider {

    private final BannerDao bannerDao;

    @Transactional(readOnly = true)
    public GetBannerRes getBannersUrl() throws BaseException {
        try {
            GetBannerRes getBannerRes = new GetBannerRes(bannerDao.findBannersUrl());
            return getBannerRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
