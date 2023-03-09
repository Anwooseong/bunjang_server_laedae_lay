package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.banner.BannerDao;
import com.example.demo.src.banner.model.GetBannerRes;
import com.example.demo.src.category.model.GetCategoryRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryProvider {

    private final CategoryDao categoryDao;

    @Transactional(readOnly = true)
    public List<GetCategoryRes> getMajorCategories() throws BaseException {
        try {
            List<GetCategoryRes> getMajorCategoriesRes = categoryDao.getMajorCategories();
            return getMajorCategoriesRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetCategoryRes> getCategoriesBelongInMajor(int majorCategoryId) throws BaseException {
        try {
            List<GetCategoryRes> getCategoriesBelongInMajorRes = categoryDao.getCategoriesBelongInMajor(majorCategoryId);
            return getCategoriesBelongInMajorRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
