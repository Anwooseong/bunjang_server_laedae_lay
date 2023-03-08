package com.example.demo.src.powerad;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.powerad.model.GetPowerAdRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PowerAdProvider {
    private final JwtService jwtService;
    private final PowerAdDao powerAdDao;

    public void getValidUser(int userId) throws BaseException{
        try {
            if (powerAdDao.getValidUser(userId) == 0) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetPowerAdRes> getPowerAd() throws BaseException{
        try {
            List<GetPowerAdRes> getPowerAdRes = powerAdDao.getPowerAd();
            return getPowerAdRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
