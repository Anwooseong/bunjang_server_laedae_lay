package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewProvider {
    private final JwtService jwtService;
    private final ReviewDao reviewDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (reviewDao.getValidUser(userId) == 0) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    public List<GetReviewRes> getReviewsByUserId(int userId) throws BaseException {
        try {
            List<GetReviewRes> getReviewsByUserIdRes = reviewDao.getReviewsByUserId(userId);
            return getReviewsByUserIdRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
