package com.example.demo.src.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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
     * [POST] /app/users/create
     * BaseResponse<PostUserRes>
     */
    @PostMapping("")
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
     * [POST] /app/users/create
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
    @PatchMapping("/{userId}/status")
    public BaseResponse<String> withDrawUser(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();  // 토큰에서 userId 추출 / 토큰 만료, 빈 토큰, 부적합 토큰 체크
            jwtService.validateUserByJwt(userIdByJwt, userId);  // user 권한만 체크

            userService.withDrawUser(userId);
            String result = "요청 성공";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 계좌 관리 조회 API
     * [GET] /app/users/{userId}/accounts
     *
     * @return BaseResponse<GetAccountRes>
     */
    @GetMapping("/{userId}/accounts")
    public BaseResponse<List<GetAccountRes>> getAccount(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            List<GetAccountRes> getAccountRes = userProvider.getAccount(userId);
            return new BaseResponse<>(getAccountRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }


    /**
     * 회원 계좌 추가 API
     * [POST] /app/users/{userId}/accounts/{accountId}
     * @return BaseResponse<PostCreateAccountRes>
     */
    @PostMapping("{userId}/accounts")
    public BaseResponse<PostCreateAccountRes> createAccount(@PathVariable("userId") int userId, @RequestBody PostCreateAccountReq postCreateAccountReq){
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            PostCreateAccountRes postCreateAccountRes = userService.createAccount(userId, postCreateAccountReq);
            return new BaseResponse<>(postCreateAccountRes);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 회원 계좌 수정 API
     * [PATCH] /app/users/{userId}/accounts/{accountId}
     * @return BaseResponse<PatchModifyAccountRes>
     */
    @PatchMapping("{userId}/accounts/{accountId}")
    public BaseResponse<PatchModifyAccountRes> modifyAccount(@PathVariable("userId") int userId, @PathVariable("accountId") int accountId, @RequestBody Account account) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            PatchModifyAccountRes patchModifyAccountRes = userService.modifyAccount(userId, accountId, account);
            return new BaseResponse<>(patchModifyAccountRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 회원 계좌 삭제 API
     * [PATCH] /app/users/{userId}/accounts/{accountId}/status
     * @return BaseResponse<PatchDeleteAccountRes>
     */
    @PatchMapping("/{userId}/accounts/{accountId}/status")
    public BaseResponse<PatchDeleteAccountRes> deleteAccount(@PathVariable("userId") int userId, @PathVariable("accountId") int accountId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);
            PatchDeleteAccountRes patchDeleteAccountRes = userService.deleteAccount(userId, accountId);
            return new BaseResponse<>(patchDeleteAccountRes);

        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
    /**
     * 회원 최근 접속일 수정 API
     * [PATCH]/app/users/{userId}/last-access
     * @return BaseResponse<PatchLastAccessRes>
     */
    @PatchMapping("/{userId}/last-access")
    public BaseResponse<PatchLastAccessRes> modifyLastAccess(@PathVariable int userId){
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            PatchLastAccessRes patchLastAccessRes = userService.modifyLastAccess(userId);
            return new BaseResponse<>(patchLastAccessRes);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 회원 찜 목록 취소 API
     * [PATCH] /app/users/{userId}/likes/{myProductId}/status
     * @return BaseResponse<String>
     */
    @PatchMapping("/{userId}/likes/{myProductId}/status")
    public BaseResponse<String> modifyUserLikeStatus(@PathVariable int userId, @PathVariable int myProductId){
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            String patchUserLikeRes = userService.modifyLikeStatus(userId, myProductId);
            return new BaseResponse<>(patchUserLikeRes);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }



    /**
     * 회원 구매 내역 조회 API
     * [GET] /app/users/{userId}/payments?type=&status=&pay=
     * @return BaseResponse<>
     */
    @GetMapping("/{userId}/payments")
    public BaseResponse<List<GetHistoryRes>> getHistory(@PathVariable int userId,
                                        @RequestParam(value = "type", defaultValue = "sale") String type,
                                        @RequestParam(value = "status", defaultValue = "all") String status,
                                        @RequestParam(value = "pay", defaultValue = "all") String pay) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            List<GetHistoryRes> getHistoryRes = userProvider.getHistory(userId, type, status, pay);
            return new BaseResponse<>(getHistoryRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }




    /**
     * 회원 정산 내역 조회 API
     * [GET] /app/users/{userId}/calculates
     * @return BaseResponse<List<GetCalculatesRes>>
     */
    @GetMapping("/{userId}/calculates")
    public BaseResponse<List<GetCalculatesRes>> getCalculates(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            List<GetCalculatesRes> getCalculatesRes = userProvider.getCalculates(userId);
            return new BaseResponse<>(getCalculatesRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 상점 상세 정보 조회 API
     * [GET] /app/users/{userId}/details
     * @return BaseResponse<GetStoreDetailRes>
     */
    @GetMapping("/{userId}/details")
    public BaseResponse<GetStoreDetailRes> getStoreDetails(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();

            GetStoreDetailRes getStoreDetailRes = userProvider.getStoreDetails(userId, userIdByJwt);

            if(userId != userIdByJwt) {
                getStoreDetailRes.setPoint(0);
            }
            else {
                getStoreDetailRes.setIsFollow("Y");
            }
            return new BaseResponse<>(getStoreDetailRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 상점 정보 조회 API
     * [GET] /app/users/{userId}
     * @return BaseResponse<GetStoreDetailRes>
     */
    @GetMapping("/{userId}")
    public BaseResponse<GetStoreRes> getStore(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();

            GetStoreRes getStoreRes = userProvider.getStore(userId, userIdByJwt);

            if(userId == userIdByJwt) {
                getStoreRes.setIsFollow("Y");
            }
            return new BaseResponse<>(getStoreRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 배송지 주소 전체 조회 API
     * [GET] /app/users/{userId}/addresses
     * @return BaseResponse<List<GetUserAddressRes>>
     */
    @GetMapping("/{userId}/addresses")
    public BaseResponse<List<GetUserAddressRes>> getUserAddresses(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            List<GetUserAddressRes> getUserAddressesRes = userProvider.getUserAddresses(userId);
            return new BaseResponse<>(getUserAddressesRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }


    /**
     * 배송지 주소 추가 API
     * [POST] /app/users/{userId}/addresses
     * @return BaseResponse<String>
     */
    @PostMapping("/{userId}/addresses")
    public BaseResponse<String> createUserAddresses(@PathVariable int userId, @RequestBody PostUserAddressReq PostUserAddressReq) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            String createUserAddressesRes = userService.createUserAddresses(userId, PostUserAddressReq);
            return new BaseResponse<>(createUserAddressesRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 배송지 주소 수정 API
     * [PATCH] /app/users/{userId}/addresses/{addressId}
     * @return BaseResponse<String>
     */
    @PatchMapping("/{userId}/addresses/{addressId}")
    public BaseResponse<String> updateUserAddresses(@PathVariable int userId, @PathVariable int addressId, @RequestBody PatchUserAddressReq patchUserAddressReq) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            String updateUserAddressesRes = userService.updateUserAddresses(userId, addressId, patchUserAddressReq);
            return new BaseResponse<>(updateUserAddressesRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }


    /**
     * 배송지 주소 삭제 API
     * [PATCH] /app/users/{userId}/addresses/{addressId}/status
     * @return BaseResponse<String>
     */
    @PatchMapping("/{userId}/addresses/{addressId}/status")
    public BaseResponse<String> deleteUserAddresses(@PathVariable int userId, @PathVariable int addressId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            String deleteUserAddressesRes = userService.deleteUserAddresses(addressId);
            return new BaseResponse<>(deleteUserAddressesRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}













