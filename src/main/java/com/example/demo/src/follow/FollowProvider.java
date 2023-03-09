package com.example.demo.src.follow;

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
public class FollowProvider {

    private final JwtService jwtService;
    private final FollowDao followDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (followDao.getValidUser(userId) == 0) {
                throw new BaseException(INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
