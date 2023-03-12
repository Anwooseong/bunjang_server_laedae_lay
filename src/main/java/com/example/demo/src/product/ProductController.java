package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.*;
import com.example.demo.src.s3.S3Uploader;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.protocol.http.AuthenticatorKeys;

import java.util.List;

@RestController
@RequestMapping("/app/products")
@RequiredArgsConstructor
public class ProductController {

    private final JwtService jwtService;
    private final ProductProvider productProvider;
    private final ProductService productService;

    /**
     * 상품 리스트 전체 조회  API
     * [GET] /app/products/main
     *
     * @return BaseResponse<GetMainProductRes>
     */
    @GetMapping("/main")
    public BaseResponse<GetMainProductRes> getMainProduct() {
        try {
            int userId = jwtService.getUserId();
            productProvider.getValidUser(userId);
            GetMainProductRes mainProduct = productProvider.getMainProduct(userId);
            return new BaseResponse<>(mainProduct);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 상품 검색  API
     * [GET] /app/products?query=&order=
     *
     * @return BaseResponse<List < GetSearchProductRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetSearchProductRes>> getSearchProduct(@RequestParam(value = "query", defaultValue = "") String search, @RequestParam(value = "order", defaultValue = "recent") String order) {
        if (!ValidationRegex.isRegexSearch(search)) {
            return new BaseResponse<>(BaseResponseStatus.GET_SEARCH_REGEX);
        }
        try {
            int userId = jwtService.getUserId();
            productProvider.getValidUser(userId);

            List<GetSearchProductRes> getSearchProductRes = productProvider.getSearchProduct(search, order);
            return new BaseResponse<>(getSearchProductRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 비슷한 상품 조회  API
     * [GET] /app/products/{productId}/similar
     *
     * @return BaseResponse<List < GetSimilarProductRes>>
     */
    @GetMapping("/{productId}/similar")
    public BaseResponse<List<GetSimilarProductRes>> getSimilarProduct(@PathVariable int productId) {
        try {
            int userId = jwtService.getUserId();
            List<GetSimilarProductRes> getSimilarProductRes = productProvider.getSimilarProduct(productId, userId);
            return new BaseResponse<>(getSimilarProductRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 판매 상품 등록  API
     * [POST] /app/products
     * @return BaseResponse<PostProductRes>
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public BaseResponse<PostProductRes> createProduct(@RequestPart(value = "file") List<MultipartFile> images,
                                                      @RequestPart(value = "postProductReq") PostProductReq postProductReq) {
        try {
            int userId = jwtService.getUserId();
            jwtService.validateUserByJwt(userId, postProductReq.getUserId());
            PostProductRes postProductRes = productService.createProduct(postProductReq, images);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
