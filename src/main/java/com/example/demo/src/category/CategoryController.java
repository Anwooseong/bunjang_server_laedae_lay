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

    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getMajorCategories() {
        try {
            List<GetCategoryRes> getMajorCategoriesRes = categoryProvider.getMajorCategories();
            return new BaseResponse<>(getMajorCategoriesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
