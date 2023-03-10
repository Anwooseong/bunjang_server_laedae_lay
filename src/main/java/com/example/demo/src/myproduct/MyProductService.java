package com.example.demo.src.myproduct;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.myproduct.model.PostMyProductReq;
import com.example.demo.src.myproduct.model.PostMyProductRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyProductService {
    private final JwtService jwtService;
    private final MyProductProvider myProductProvider;
    private final MyProductDao myProductDao;

    public PostMyProductRes createMyProduct(PostMyProductReq postMyProductReq) throws BaseException {
        if (myProductDao.checkMyProduct(postMyProductReq) == 1) {
            throw new BaseException(BaseResponseStatus.POST_MY_PRODUCT_EXISTS);
        }
        try {
            int id = myProductDao.createMyProduct(postMyProductReq);
            return new PostMyProductRes(id, "찜 추가 성공했습니다.");
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
