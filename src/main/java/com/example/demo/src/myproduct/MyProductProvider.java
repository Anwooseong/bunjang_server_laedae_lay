package com.example.demo.src.myproduct;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_JWT;

@Service
@Transactional
@RequiredArgsConstructor
public class MyProductProvider {
    private final JwtService jwtService;
    private final MyProductDao myProductDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (myProductDao.getValidUser(userId) == 0) {
                throw new BaseException(INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
