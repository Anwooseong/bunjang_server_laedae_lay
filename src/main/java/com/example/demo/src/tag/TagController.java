package com.example.demo.src.tag;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.tag.model.GetTagRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/tags")
@RequiredArgsConstructor
public class TagController {

    private final JwtService jwtService;
    private final TagService tagService;
    private final TagProvider tagProvider;

    @GetMapping("")
    public BaseResponse<List<GetTagRes>> searchTags(@RequestParam("query") String search){
        if (!ValidationRegex.isRegexSearch(search)) {
            return new BaseResponse<>(BaseResponseStatus.GET_SEARCH_REGEX);
        }
        try {
            int userId = jwtService.getUserId();
            tagProvider.getValidUser(userId);
            List<GetTagRes> getTagRes = tagProvider.getSearchResult(search);
            return new BaseResponse<>(getTagRes);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
