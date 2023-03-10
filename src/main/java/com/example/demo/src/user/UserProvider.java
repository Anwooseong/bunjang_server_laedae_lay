package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
@RequiredArgsConstructor
@Transactional
public class UserProvider {


    private final UserDao userDao;
    private final JwtService jwtService;

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try {
            return userDao.checkPhoneNumber(phoneNumber);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReportUser(String phoneNumber, String name) throws BaseException {
        try {
            return userDao.checkReportUser(phoneNumber, name);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUid(String uid) throws BaseException{
        try {
            return userDao.checkUid(uid);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkStoreName(String randomStoreName) throws BaseException{
        try {
            return userDao.checkStoreName(randomStoreName);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {

        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;

        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR); // 4012, "비밀번호 복호화에 실패하였습니다."
        }
        if (userDao.checkLoginReportUser(postLoginReq, encryptPwd) == 1) {
            throw new BaseException(POST_USERS_REPORT_USER);
        }
        if (userDao.checkUid(postLoginReq.getUid()) == 0) {
            throw new BaseException(FAILED_TO_LOGIN); // 3014,"없는 아이디거나 비밀번호가 틀렸습니다."
        }
        if (user.getPassword().equals(encryptPwd)) {
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, user.getStoreName(), user.getName(), jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN); // 3014,"없는 아이디거나 비밀번호가 틀렸습니다."
        }
    }

    public List<GetAccountRes> getAccount(int userId) throws BaseException{
        try {
            List<GetAccountRes> getAccountRes = userDao.getAccount(userId);
            return getAccountRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
