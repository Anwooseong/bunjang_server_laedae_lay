package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.example.demo.utils.ValidationRegex;
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
        if (userProvider.checkReportUser(postUserReq.getPhoneNumber(), postUserReq.getName()) == 1){
            throw new BaseException(POST_USERS_REPORT_USER); // 2029, "신고로 정지당한 회원입니다."
        }
        if (userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE); // 2017,"중복된 전화번호입니다."
        }
        if (userProvider.checkUid(postUserReq.getUid()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_UID); // 2031,"중복된 아이디입니다."
        }
        String password;
        try {
            //암호화
            password = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(password);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR); // 4011, "비밀번호 암호화에 실패하였습니다."
        }

        //상점명 랜덤기입
        boolean checkStore = true;
        String baseStoreName = "";
        while (checkStore) {
            int randomValue = (int)(Math.random()*10000+1 -1000)+1000; //1000~10000랜덤수
            String randomStoreName = "가게"+randomValue;
            if (userProvider.checkStoreName(randomStoreName) == 0) {
                baseStoreName = randomStoreName;
                checkStore = false;
            }
        }

        try {
            int userId = userDao.createUser(postUserReq, baseStoreName);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(userId, baseStoreName, postUserReq.getName(), jwt);
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

    //최대2개, 기본값설정시 원래있던거는 해제, 있는 계좌인지, 예금주 특수문자예외처리 (예금주, 은행, 계좌번호, 기본계좌 설정 유무)
    public PostCreateAccountRes createAccount(int userId, PostCreateAccountReq postCreateAccountReq) throws BaseException {
        if (userDao.checkMaximumAccount(userId) == 1){
            throw new BaseException(POST_ACCOUNT_MAXIMUM);
        }
        if (userDao.checkAccount(userId, postCreateAccountReq.getAccountNumber()) == 1){
            throw new BaseException(POST_ACCOUNT_EXISTS);
        }
        if (!ValidationRegex.isRegexHolderName(postCreateAccountReq.getHolderName())) {
            throw new BaseException(POST_ACCOUNT_HOLDER_NAME_REGEX);
        }
        if (!ValidationRegex.isRegexAccount(postCreateAccountReq.getAccountNumber())) {
            throw new BaseException(POST_ACCOUNT_NUMBER_REGEX);
        }

        try {

            int createId = userDao.createAccount(userId, postCreateAccountReq);
            if (createId == 0) {
                throw new BaseException(POST_CREATE_ACCOUNT_FAIL);
            }
            if (postCreateAccountReq.isDefaultAccountCheck()){
                int result = userDao.modifyDefaultAccount(userId, createId);
                if (result == 0) {
                    throw new BaseException(POST_CREATE_DEFAULT_ACCOUNT_FAIL);
                }
            }
            return new PostCreateAccountRes(createId, "정상적으로 계좌 추가되었습니다.");
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchModifyAccountRes modifyAccount(int userId, int accountId, Account account) throws BaseException{
        if (!ValidationRegex.isRegexHolderName(account.getHolderName())) {
            throw new BaseException(POST_ACCOUNT_HOLDER_NAME_REGEX);
        }
        if (!ValidationRegex.isRegexAccount(account.getAccountNumber())) {
            throw new BaseException(PATCH_ACCOUNT_NUMBER_REGEX);
        }
        try {
            if (account.isDefaultAccountCheck()) {
                int isSuccess =userDao.modifyDefaultAccount(userId, accountId);
                if (isSuccess == 0) {
                    throw new BaseException(PATCH_MODIFY_DEFAULT_ACCOUNT_FAIL);
                }
            }
            int result = userDao.modifyAccount(userId, accountId, account);
            if (result == 0) {
                throw new BaseException(PATCH_MODIFY_ACCOUNT_FAIL);
            }
            return new PatchModifyAccountRes(accountId, "계좌 수정 완료하였습니다.");
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
