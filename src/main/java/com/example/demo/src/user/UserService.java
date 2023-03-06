package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

/**
 * Service란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    // 회원가입(POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        String phoneNumber;
        if (userProvider.checkStoreName(postUserReq.getStoreName()) == 1){ //상점명 중복
            throw new BaseException(POST_USERS_EXISTS_STORE_NAME);
        }
        try {
            //암호화
            phoneNumber = new SHA256().encrypt(postUserReq.getPhoneNumber());
            postUserReq.setPhoneNumber(phoneNumber);
        } catch (Exception ignored) {
            throw new BaseException(PHONE_ENCRYPTION_ERROR);
        }
        if (userProvider.checkReportUser(postUserReq.getPhoneNumber()) == 1){
            throw new BaseException(POST_USERS_REPORT_USER);
        }
        if (userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        try {
            int userId = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(userId, postUserReq.getStoreName(), postUserReq.getName(), jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void withDrawUser(int userIdx) throws BaseException {
        try {
            int result = userDao.withDrawUser(userIdx);
            if(result == 0) {
                throw new BaseException(POST_USERS_WITHDRAW_FAIL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
