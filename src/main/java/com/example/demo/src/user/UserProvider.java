package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
