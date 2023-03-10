package com.example.demo.src.delivery;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.delivery.model.GetDeliveryRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryProvider {
    private final JwtService jwtService;
    private final DeliveryDao deliveryDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (deliveryDao.getValidUser(userId) == 0) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetDeliveryRes> getDelivery(int userId, String status) throws BaseException{
        if (!(status.equals("send") || status.equals("receive") || status.equals("register"))) {
            throw new BaseException(BaseResponseStatus.GET_DELIVERY_PARAMETER);
        }
        try {
            String input;
            if (status.equals("send")) {
                input = "S";
            } else if (status.equals("receive")) {
                input = "R";
            }else {
                input = "A";
            }
            List<GetDeliveryRes> getDeliveryRes = deliveryDao.getDelivery(userId, input);
            for (GetDeliveryRes getDeliveryRe : getDeliveryRes) {
                String url = deliveryDao.getImageUrl(getDeliveryRe.getProductId());
                getDeliveryRe.setUrl(url);
            }
            return getDeliveryRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
