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

    public void getValidUser(int userId) throws BaseException{
        try {
            if(bannerDao.getValidUser(userId) == 0){
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
