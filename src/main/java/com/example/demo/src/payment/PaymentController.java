package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.PostPaymentReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final JwtService jwtService;
    private final PaymentService paymentService;
    private final  PaymentProvider paymentProvider;

    /**
     * 상품 결제 API
     * [POST] /app/payments/{productId}
     * @return BaseResponse<PostPaymentRes>
     */
    @PostMapping("/{productId}")
    public BaseResponse<PostPaymentRes> createPayment(@PathVariable int productId, @RequestBody PostPaymentReq postPaymentReq){
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, postPaymentReq.getUserId());

            PostPaymentRes postPaymentRes = paymentService.createPayment(productId, postPaymentReq);
            return new BaseResponse<>(postPaymentRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
