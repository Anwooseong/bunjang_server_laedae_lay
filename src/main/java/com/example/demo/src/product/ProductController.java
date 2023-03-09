package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.dto.MainProductDto;
import com.example.demo.src.product.model.GetMainProductRes;
import com.example.demo.src.tag.model.GetTagRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * [GET] /app/products
     * @return BaseResponse<GetMainProductRes>
     */
    @GetMapping("")
    public BaseResponse<GetMainProductRes> getMainProduct(){
        try {
            int userId = jwtService.getUserId();
            productProvider.getValidUser(userId);
            GetMainProductRes mainProduct = productProvider.getMainProduct(userId);
            return new BaseResponse<>(mainProduct);
        }catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
