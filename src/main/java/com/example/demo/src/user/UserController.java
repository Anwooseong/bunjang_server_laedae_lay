package com.example.demo.src.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.web.bind.annotation.*;



import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
@RequiredArgsConstructor
public class UserController {
    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 회원가입 API
     * [POST] /create
     * BaseResponse<PostUserRes>
     */
    //TODO REGEX
    @PostMapping("/create")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        if (postUserReq.getPhoneNumber().length() < 4){
            return new BaseResponse<>(POST_USERS_COUNT_PHONE);
        }
        if (!isRegexPhone(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_PHONE_REGEX);
        }
        if (!isRegexBirth(postUserReq.getBirth())){
            return new BaseResponse<>(POST_USERS_BIRTH_REGEX);
        }
        if (postUserReq.getStoreName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_STORE_NAME);
        }
        if (postUserReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if (postUserReq.getName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        if (postUserReq.getBirth() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTH);
        }
        if (postUserReq.getCarrier() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_CARRIER);
        }
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 탈퇴 API
     * [PATCH] /users/d
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/d")
    public BaseResponse<String> withDrawUser() {
        try {
            int userIdxByJwt = jwtService.getUserId();
            userService.withDrawUser(userIdxByJwt);

            String result = "요청 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
