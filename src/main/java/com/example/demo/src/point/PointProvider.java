package com.example.demo.src.point;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.point.dto.DetailPoint;
import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointProvider {
    private final JwtService jwtService;
    private final PointDao pointDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (pointDao.getValidUser(userId) == 0) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public GetPointRes getPoints(int userId, String status) throws BaseException{
        if (!(status.equals("all")||status.equals("A")||status.equals("U")||status.equals("R"))){
            throw new BaseException(BaseResponseStatus.GET_POINT_STATUS);
        }
        try {
            String totalPoint = pointDao.getTotalPoint(userId);
            if (status.equals("all")){
                List<DetailPoint> detailPoints = pointDao.getDetailPointAll(userId);
                return new GetPointRes(totalPoint, detailPoints);
            }
            List<DetailPoint> detailPoints = pointDao.getDetailPointStatus(userId, status);
            return new GetPointRes(totalPoint, detailPoints);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }
}
