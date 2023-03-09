package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.brand.model.GetBrandRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.utils.ValidationRegex.isRegexSearch;

@RestController
@RequestMapping("/app/brands")
@RequiredArgsConstructor
public class BrandController {

    private final JwtService jwtService;
    private final BrandProvider brandProvider;
    private final BrandService brandService;

    /**
     * 브랜드 전체 조회(검색 포함, 나의 팔로잉 여부) API
     * [GET] /app/brands
     * @return BaseResponse<List<GetBrandRes>>
     */
    @GetMapping("{userId}")
    public BaseResponse<List<GetBrandRes>> getBrands(@RequestParam(value = "order", defaultValue = "ko", required = false) String order,
                                                     @RequestParam(value = "following", defaultValue = "N", required = false) String following,
                                                     @RequestParam(value = "query", defaultValue = "", required = false) String search,
                                                     @PathVariable int userId) {
        if (!search.equals("")) {
            if (!isRegexSearch(search)) {
                return new BaseResponse<>(BaseResponseStatus.GET_SEARCH_REGEX);
            }
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetBrandRes> getBrandRes = brandProvider.getResultBrand(userId, order, following, search);
            return new BaseResponse<>(getBrandRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
