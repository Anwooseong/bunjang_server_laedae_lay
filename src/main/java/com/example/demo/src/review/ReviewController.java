package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final JwtService jwtService;
    private final ReviewProvider reviewProvider;
    private final ReviewService reviewService;


    /**
     * 상품 별점 후기 작성 API
     * [POST] /app/reviews
     * @return BaseResponse<PostReviewRes>
     */
    @PostMapping("")
    public BaseResponse<PostReviewRes> createReview(@RequestBody PostReviewReq postReviewReq){
        try {
            if (postReviewReq.getContent() == null) {
                return new BaseResponse<>(BaseResponseStatus.POST_REVIEW_EMPTY_CONTENT);
            }
            if (postReviewReq.getStarRating() > 5 || postReviewReq.getStarRating() < 0){
                return new BaseResponse<>(BaseResponseStatus.POST_REVIEW_STAR_SIZE);
            }
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != postReviewReq.getBuyerId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostReviewRes postReviewRes = reviewService.createReview(postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }


}
