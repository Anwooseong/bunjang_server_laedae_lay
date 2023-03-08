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
    @PostMapping("/create")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        if (postUserReq.getPhoneNumber().length() < 4){
            return new BaseResponse<>(POST_USERS_COUNT_PHONE); // 2024, "휴대폰 전화번호 4자리 이상 적어주세요."
        }
        if (!isRegexPhone(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_PHONE_REGEX); // 2025, "휴대폰 전화번호 -개수와 숫자를 3, 4, 4로 맞춰주세요."
        }
        if (!isRegexUid(postUserReq.getUid())) {
            return new BaseResponse<>(POST_USERS_UID_REGEX); // 2030, "영문과 숫자의 조합으로 5글자에서 8글자로 맞춰주세요."
        }
        if (!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_PASSWORD_REGEX);  // 2026, "최소 8 자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자 정규식"
        }
        if (postUserReq.getName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME); // 2021, "이름 입력칸이 비어 있습니다."
        }
        if (postUserReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER); // 2020, "전화번호 입력칸이 비어 있습니다."
        }
        if (postUserReq.getUid() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_UID); // 2022, "아이디 입력칸이 비어 있습니다."
        }
        if (postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD); // 2023, "비밀번호 입력칸이 비어 있습니다."
        }
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /create
     * BaseResponse<PostUserRes>
     */
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> loginUser(@RequestBody PostLoginReq postLoginReq) {
        if (postLoginReq.getUid() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_UID); // 2022, "아이디 입력칸이 비어 있습니다."
        }
        if (postLoginReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD); // 2023, "비밀번호 입력칸이 비어 있습니다."
        }
        if (!isRegexUid(postLoginReq.getUid())) {
            return new BaseResponse<>(POST_USERS_UID_REGEX); // 2030, "영문과 숫자의 조합으로 5글자에서 8글자로 맞춰주세요."
        }
        if (!isRegexPassword(postLoginReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_PASSWORD_REGEX);  // 2026, "최소 8 자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자 정규식"
        }
        try {
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 탈퇴 API
     * [PATCH] /users/{userId}/delete
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}/delete")
    public BaseResponse<String> withDrawUser(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.withDrawUser(userIdByJwt);

            String result = "요청 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
