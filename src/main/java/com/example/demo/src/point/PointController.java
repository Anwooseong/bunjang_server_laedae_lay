package com.example.demo.src.point;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.src.product.model.GetMainProductRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/bungae-points")
@RequiredArgsConstructor
public class PointController {
    private final JwtService jwtService;
    private final PointService pointService;
    private final PointProvider pointProvider;

    @GetMapping("{userId}/details")
    public BaseResponse<GetPointRes> getPoint(@PathVariable int userId, @RequestParam(value = "status", defaultValue = "all") String status){

        try {
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPointRes getPointRes = pointProvider.getPoints(userId, status);
            return new BaseResponse<>(getPointRes);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
