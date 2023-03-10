package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.PostFollowerReq;
import com.example.demo.src.follow.model.PostFollowerRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app") //uri -> 팔로잉이랑 팔로워 각각 있기 때문에 /app으로만 작성하였음
@RequiredArgsConstructor
public class FollowController {
    private final JwtService jwtService;
    private final FollowProvider followProvider;
    private final FollowService followService;

    /**
     * 상점 팔로우 API
     * [POST] /app/followers
     * @return BaseResponse<PostFollowerRes>
     */
    @PostMapping("/followers")
    public BaseResponse<PostFollowerRes> createFollower(@RequestBody PostFollowerReq postFollowerReq){
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != postFollowerReq.getFollowerId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostFollowerRes postFollowerRes = followService.createFollower(postFollowerReq);
            return new BaseResponse<>(postFollowerRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

}
