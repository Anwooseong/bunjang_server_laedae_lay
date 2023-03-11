package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.dto.MainProductDto;
import com.example.demo.src.product.model.GetMainProductRes;
import com.example.demo.src.product.model.GetSearchProductRes;
import com.example.demo.src.product.model.GetSimilarProductRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductProvider {

    private final JwtService jwtService;
    private final ProductDao productDao;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (productDao.getValidUser(userId) == 0) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public GetMainProductRes getMainProduct(int userId) throws BaseException{
        try {
            if (productDao.getValidUser(userId) == 0) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            List<MainProductDto> manPadding = getMainProductDtos("남성의류", "패딩/점퍼");
            List<MainProductDto> womenPadding = getMainProductDtos("여성의류", "패딩/점퍼");
            List<MainProductDto> manShoes = getMainProductDtos("신발", "남성화");
            List<MainProductDto> womenShoes = getMainProductDtos("신발", "여성화");
            List<MainProductDto> sneakers = getMainProductDtos("신발", "스니커즈");
            GetMainProductRes getMainProductRes = new GetMainProductRes(manPadding, womenPadding, manShoes, womenShoes, sneakers);
            return getMainProductRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private List<MainProductDto> getMainProductDtos(String major, String middle) {
        List<MainProductDto> dtos = productDao.getMainProduct(major, middle);
        getImageUrl(dtos);
        return dtos;
    }

    private void getImageUrl(List<MainProductDto> category) {
        for (MainProductDto mainProductDto : category) {
            int id = mainProductDto.getId();
            String url = productDao.getImageUrl(id);
            mainProductDto.setUrl(url);
        }
    }

    public List<GetSearchProductRes> getSearchProduct(String search, String order) throws BaseException{ //order = "recent"(최신순), "price-asc"(저가순), "price-desc"(고가순)
        if (!(order.equals("recent") || order.equals("price-asc") || order.equals("price-desc"))) {
            throw new BaseException(BaseResponseStatus.GET_SEARCH_PRODUCT_PARAMETER);
        }
        String param;
        if (order.equals("recent")) {
            param="updated_at desc";
        } else if (order.equals("price-asc")) {
            param="price asc";
        } else {
            param="price desc";
        }
        try {
            List<GetSearchProductRes> getSearchProductRes = productDao.getSearchProduct(search, param);
            for (GetSearchProductRes getSearchProductRe : getSearchProductRes) {
                String imageUrl = productDao.getImageUrl(getSearchProductRe.getProductId());
                getSearchProductRe.setUrl(imageUrl);
            }
            return getSearchProductRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetSimilarProductRes> getSimilarProduct(int productId, int userId) throws BaseException{
        try {
            List<GetSimilarProductRes> getSimilarProductRes = productDao.getSimilarProduct(productId);
            for (GetSimilarProductRes getSimilarProductRe : getSimilarProductRes) {
                String imageUrl = productDao.getImageUrl(getSimilarProductRe.getProductId());
                getSimilarProductRe.setImageUrl(imageUrl);
            }
            for (GetSimilarProductRes getSimilarProductRe : getSimilarProductRes) {
                if (productDao.getMyProduct(productId, userId) == 1) {
                    getSimilarProductRe.setCheckMyProduct(true);
                }
                else{
                    getSimilarProductRe.setCheckMyProduct(false);
                }
            }
            return getSimilarProductRes;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
