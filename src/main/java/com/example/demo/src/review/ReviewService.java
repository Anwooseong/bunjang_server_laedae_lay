package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final JwtService jwtService;
    private final ReviewProvider reviewProvider;
    private final ReviewDao reviewDao;
    public PostReviewRes createReview(PostReviewReq postReviewReq) throws BaseException {
        try {
            int id = reviewDao.createReview(postReviewReq);
            return new PostReviewRes(id, "정상적으로 작성되었습니다.");
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }
}
