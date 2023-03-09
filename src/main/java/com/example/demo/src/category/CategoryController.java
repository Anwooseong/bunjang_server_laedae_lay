package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.banner.model.GetBannerRes;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/categories")
@RequiredArgsConstructor
public class CategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryProvider categoryProvider;
    private final CategoryService categoryService;
    private final JwtService jwtService;

    /**
     * 대분류 카테고리 이름 전체 API
     * [GET] /app/categories
     * @return BaseResponse<GetCategoryRes>
     */
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getMajorCategories() {
        try {
            List<GetCategoryRes> getMajorCategoriesRes = categoryProvider.getMajorCategories();
            return new BaseResponse<>(getMajorCategoriesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 특정 대분류 카테고리에 속하는 중분류 카테고리 이름 전체 조회 API
     * [GET] /app/categories/:majorCategoryId
     * @return BaseResponse<GetCategoryRes>
     */
    @GetMapping("/{majorCategoryId}")
    public BaseResponse<List<GetCategoryRes>> getCategoriesBelongInMajor(@PathVariable("majorCategoryId") int majorCategoryId) {
        try {
            List<GetCategoryRes> getCategoriesBelongInMajorRes = categoryProvider.getCategoriesBelongInMajor(majorCategoryId);
            return new BaseResponse<>(getCategoriesBelongInMajorRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
