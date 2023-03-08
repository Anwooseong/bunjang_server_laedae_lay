package com.example.demo.src.tag;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.tag.model.GetTagRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagProvider {

    private final TagDao tagDao;
    private final JwtService jwtService;

    public GetTagRes getSearchResult(String search) throws BaseException {
        try {
            GetTagRes getTagRes = new GetTagRes(tagDao.getSearchResult(search));
            return getTagRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }
}
