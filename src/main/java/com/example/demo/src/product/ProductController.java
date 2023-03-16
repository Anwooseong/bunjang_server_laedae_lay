package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    public BaseResponse<List<GetSearchProductRes>> getSearchProduct(@RequestParam(value = "query", required = false) String search, @RequestParam(value = "order", required = false) String order) {
        if (search == null) {
            return new BaseResponse<>(GET_PRODUCT_QUERY_PARAMETER);
        }
        if (order == null) {
            return new BaseResponse<>(GET_PRODUCT_ORDER_PARAMETER);
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

    /**
     * 상품 정보 조회  API
     * [GET] /app/products/{productId}
     *
     * @return BaseResponse<GetProductDetailRes>
     */
    @GetMapping("/{productId}")
    public BaseResponse<GetProductDetailRes> getProductDetail(@PathVariable int productId) {
        try {
            jwtService.validateTokenExpired();
            GetProductDetailRes getProductDetailRes = productProvider.getProductDetail(productId);
            return new BaseResponse<>(getProductDetailRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 내 상품 정보 상태 변경 API
     * [PATCH] /app/products/:productId?status=
     * @return BaseResponse<String>
     */
    @PatchMapping("/{productId}")
    public BaseResponse<String> modifyProductStatus(@PathVariable("productId") int productid, @RequestParam(value = "status", required = true) String status){
        try {
            if(status.equals("for-sale") || status.equals("reserved") || status.equals("sold-out")) {
                jwtService.validateTokenExpired();

                String patchProductStatusRes = productService.modifyProductStatus(productid, status);
                return new BaseResponse<>(patchProductStatusRes);
            }
            else {
                throw new BaseException(PATCH_PRODUCT_STATUS_INVALID_OR_EMPTY_PARAMETER);
            }
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 판매 상품 내역 조회 API
     * [GET] /app/products/:userId/items?order=&status=
     *
     * @return BaseResponse<GetProductSimpleRes>
     */
    @GetMapping("/{userId}/items")
    public BaseResponse<List<GetProductInRowRes>> getProductsByUserId(@PathVariable int userId,
                                                                      @RequestParam(value = "order", required = false, defaultValue = "recent") String order,
                                                                      @RequestParam(value = "status", required = false, defaultValue = "for-sale") String status) {
        ArrayList<String> orderValues = new ArrayList<>(Arrays.asList("recent", "popular", "low", "high"));
        ArrayList<String> statusValues = new ArrayList<>(Arrays.asList("for-sale", "all", "reserved", "sold-out", "pay-avail", "ad"));

        try {
            jwtService.validateTokenExpired();

            if(!orderValues.contains(order)) {
                throw new BaseException(GET_PRODUCT_INVALID_QUERY_STRING_ORDER);
            }
            if(!statusValues.contains(status)) {
                throw new BaseException(GET_PRODUCT_INVALID_QUERY_STRING_STATUS);
            }
            List<GetProductInRowRes> getProductsByUserIdRes = productProvider.getProductsByUserId(userId, order, status);
            return new BaseResponse<>(getProductsByUserIdRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 판매 상품 등록  API
     * [POST] /app/products/enrollment
     * @return BaseResponse<PostProductDetailRes>
     */
    @PostMapping("/enrollment")
    public BaseResponse<PostProductDetailRes> createProduct(@RequestBody PostProductReq postProductReq) {
        try {
            int userId = jwtService.getUserId();
            jwtService.validateUserByJwt(userId, postProductReq.getUserId());
            PostProductDetailRes postProductRes = productService.createProduct(postProductReq);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }


    /**
     *  내 상품 정보 수정  API
     * [PATCH] /app/products/:productId
     * @return BaseResponse<String>
     */
    @PatchMapping(value="/{productId}", consumes = {"multipart/form-data"})
    public BaseResponse<String> updateProduct(@PathVariable int productId,
                                                      @RequestPart(value = "file") List<MultipartFile> images,
                                              @RequestPart(value = "patchProductReq") PatchProductReq patchProductReq) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, patchProductReq.getUserId());

            String postProductRes = productService.updateProduct(productId, patchProductReq, images);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
