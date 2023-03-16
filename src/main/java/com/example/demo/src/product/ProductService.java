package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.PostProductDetailRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.s3.S3Uploader;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_PRODUCT_STATUS_FAIL;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final JwtService jwtService;
    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final S3Uploader s3Uploader;


    public PostProductRes createProduct(PostProductReq postProductReq, List<MultipartFile> images) throws BaseException {
        if (images.size() > 5){
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_IMAGE_SIZE);
        }
        if (postProductReq.getAmount() < 1){
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_AMOUNT_POSITIVE);
        }
        if (postProductReq.getTagIds().size() > 6){
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_TAG_SIZE);
        }
        try {
            int lastInsertId = productDao.createProduct(postProductReq);
            List<String> imageUrl = s3Uploader.uploadFile(images, "items/"+lastInsertId, lastInsertId);
            List<String> resultUrl = new ArrayList<>();
            for (String image : imageUrl) {
                if (image == null) break;
                resultUrl.add(image);
                int result = productDao.createProductImage(lastInsertId, image);
                if (result == 0){
                    throw new BaseException(BaseResponseStatus.POST_PRODUCT_IMAGE_UPLOAD);
                }
            }
            for (Integer tagId : postProductReq.getTagIds()) {
                productDao.createProductTag(lastInsertId, tagId);
            }
            return new PostProductRes(lastInsertId, resultUrl);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String modifyProductStatus(int productId, String status) throws BaseException {
        try {
            switch (status) {
                case "sold-out":
                    status = "SO";
                    break;
                case "for-sale":
                    status = "FS";
                    break;
                case "reserved":
                    status = "R";
                    break;
            }

            int result = productDao.modifyProductStatus(productId, status);
            if(result == 0) {
                throw new BaseException(MODIFY_PRODUCT_STATUS_FAIL);
            }
            return "상품 상태 변경에 성공하였습니다.";
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostProductDetailRes createProduct(PostProductReq postProductReq) throws BaseException{
        if (postProductReq.getAmount() < 1){
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_AMOUNT_POSITIVE);
        }
        if (postProductReq.getTagIds().size() > 6){
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_TAG_SIZE);
        }
        try {
            int lastInsertId = productDao.createProduct(postProductReq);
            productDao.createProductImage(lastInsertId, "https://bungae-bucket.s3.ap-northeast-2.amazonaws.com/b1.jpg");
            productDao.createProductImage(lastInsertId, "https://bungae-bucket.s3.ap-northeast-2.amazonaws.com/b2.jpg");
            productDao.createProductImage(lastInsertId, "https://bungae-bucket.s3.ap-northeast-2.amazonaws.com/b3.jpg");
            productDao.createProductImage(lastInsertId, "https://bungae-bucket.s3.ap-northeast-2.amazonaws.com/b4.jpg");
            productDao.createProductImage(lastInsertId, "https://bungae-bucket.s3.ap-northeast-2.amazonaws.com/b5.jpg");

            for (Integer tagId : postProductReq.getTagIds()) {
                productDao.createProductTag(lastInsertId, tagId);
            }
            return new PostProductDetailRes(lastInsertId);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
