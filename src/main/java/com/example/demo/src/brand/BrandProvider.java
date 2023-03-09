package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.brand.model.GetBrandRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandProvider {
    private final JwtService jwtService;
    private final BrandDao brandDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (brandDao.getValidUser(userId) == 0) {
                throw new BaseException(INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrandRes> getResultBrand(int userId, String order, String following, String search) throws BaseException {
        if (!(order.equals("ko") || order.equals("en"))) {
            throw new BaseException(GET_BRAND_ORDER);
        }
        if (!(following.equals("N") || following.equals("Y"))) {
            throw new BaseException(GET_BRAND_FOLLOWING);
        }
        try {
            if (following.equals("N")) {
                List<GetBrandRes> getResultBrandNotFollow;
                if (order.equals("ko")) {
                    getResultBrandNotFollow = brandDao.getResultBrandNotFollowKo(userId, search);
                } else {
                    getResultBrandNotFollow = brandDao.getResultBrandNotFollowEn(userId, search);
                }
                return getResultBrandNotFollow;
            } else {
                List<GetBrandRes> getResultBrandFollow;
                if (order.equals("ko")) {
                    getResultBrandFollow = brandDao.getResultBrandFollowKo(userId, search);
                } else {
                    getResultBrandFollow = brandDao.getResultBrandFollowEn(userId, search);
                }
                return getResultBrandFollow;
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
