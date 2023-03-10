package com.example.demo.src.delivery;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.delivery.model.GetDeliveryRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final JwtService jwtService;
    private final DeliveryProvider deliveryProvider;
    private final DeliveryService deliveryService;


    /**
     * 배송 조회 API
     * [GET] /app/delivery/{userId}?status=
     * @return BaseResponse<List<GetDeliveryRes>>
     */
    @GetMapping("/{userId}")
    public BaseResponse<List<GetDeliveryRes>> getDelivery(@PathVariable int userId, @RequestParam(value = "status", defaultValue = "send") String status){
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            List<GetDeliveryRes> getDeliveryRes = deliveryProvider.getDelivery(userId, status);
            return new BaseResponse<>(getDeliveryRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
