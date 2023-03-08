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

import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/tags")
@RequiredArgsConstructor
public class TagController {

    private final JwtService jwtService;
    private final TagService tagService;
    private final TagProvider tagProvider;

    @GetMapping("")
    public BaseResponse<GetTagRes> searchTags(@RequestParam("query") String search){
        System.out.println(search);
        if (!isRegexSearch(search)){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_SEARCH_REGEX);
        }
        try {
            GetTagRes getTagRes = tagProvider.getSearchResult(search);
            return new BaseResponse<>(getTagRes);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
