package com.example.demo.src.myproduct;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.myproduct.model.PostMyProductReq;
import com.example.demo.src.myproduct.model.PostMyProductRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/my-products")
@RequiredArgsConstructor
public class MyProductController {
    private final JwtService jwtService;
    private final MyProductProvider myProductProvider;
    private final MyProductService myProductService;

    /**
     * 회원 찜 추가 API
     * [POST] /app/my-products
     * @return BaseResponse<PostMyProductRes>
     */
    @PostMapping("")
    public BaseResponse<PostMyProductRes> createMyProduct(@RequestBody PostMyProductReq postMyProductReq) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, postMyProductReq.getUserId());

            PostMyProductRes postMyProductRes = myProductService.createMyProduct(postMyProductReq);
            return new BaseResponse<>(postMyProductRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

}
