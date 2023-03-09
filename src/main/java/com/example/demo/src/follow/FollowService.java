package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.PostFollowerReq;
import com.example.demo.src.follow.model.PostFollowerRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final JwtService jwtService;
    private final FollowProvider followProvider;
    private final FollowDao followDao;

    public PostFollowerRes createFollower(PostFollowerReq postFollowerReq) throws BaseException {
        try {
            int id = followDao.createFollower(postFollowerReq);
            return new PostFollowerRes(id, "정상적으로 팔로우가 되었습니다.");
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
