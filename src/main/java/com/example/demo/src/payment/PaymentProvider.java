package com.example.demo.src.payment;

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
public class PaymentProvider {
    private final JwtService jwtService;
    private final PaymentDao paymentDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (paymentDao.getValidUser(userId) == 0) {
                throw new BaseException(INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
