package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.payment.model.PostPaymentReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.POST_PAYMENT_AGREEMENT;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final JwtService jwtService;
    private final PaymentProvider paymentProvider;
    private final PaymentDao paymentDao;

    public PostPaymentRes createPayment(int productId, PostPaymentReq postPaymentReq) throws BaseException {
        if (postPaymentReq.getPaymentMethod() == null) {
            throw new BaseException(POST_PAYMENT_METHOD_EMPTY);
        }
        if (!postPaymentReq.isAgreement()) {
            throw new BaseException(POST_PAYMENT_AGREEMENT);
        }
        try {
            int totalPoint = paymentDao.getTotalPoint(postPaymentReq.getUserId());
            if (postPaymentReq.getUsedPoint() > totalPoint){
                throw new BaseException(POST_PAYMENT_POINT_LITTLE);
            }
        } catch (Exception e) {
            throw new BaseException(POST_PAYMENT_POINT_LITTLE);
        }
        try {

            int lastInsertedId = paymentDao.createPayment(productId, postPaymentReq);
            PostPaymentRes postPaymentRes = paymentDao.getPayment(lastInsertedId);
            return postPaymentRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
