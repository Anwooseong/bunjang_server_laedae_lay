package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.product.model.PostProductDetailRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.s3.S3Uploader;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    final Logger logger = LoggerFactory.getLogger(this.getClass());
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

    @Transactional(rollbackOn = BaseException.class)
    public String updateProduct(int productId, PatchProductReq patchProductReq, List<MultipartFile> images) throws BaseException {
        try {
            // 상품 정보 Update
            int updateProductResult = productDao.updateProduct(productId, patchProductReq);

            if(updateProductResult == 0) {
                throw new BaseException(BaseResponseStatus.PATCH_USER_PRODUCT_UPDATE_FAIL);
            }

            // productId에 해당하는 모든 이미지 삭제 및 새로운 이미지 업로드
            List<String> imageUrl = s3Uploader.uploadFile(images, "items/" + productId, productId);
            List<String> resultUrl = new ArrayList<>();


            // productId를 가진 이미지 URL 저장 레코드 모두 삭제
            int deleteProductImgAllResult = productDao.deleteProductImgAll(productId);

            if(deleteProductImgAllResult == 0) {
                throw new BaseException(BaseResponseStatus.PATCH_PRODUCT_IMG_DELETE_ALL_FAIL);
            }

            // 반환받은 S3 이미지 URL 리스트를 하나씩 새로 Insert
            for (String image : imageUrl) {
                if (image == null)
                    break;
                resultUrl.add(image);

                int updateProductImgResult = productDao.createProductImage(productId, image);
                if (updateProductImgResult == 0){
                    throw new BaseException(BaseResponseStatus.PATCH_USER_PRODUCT_IMG_CREATE_FAIL);
                }
            }

            // productId에 해당하는 모든 태그 삭제
            int deleteProductTagAllResult = productDao.deleteProductTagAll(productId);
            if(deleteProductTagAllResult == 0) {
                throw new BaseException(BaseResponseStatus.PATCH_USER_PRODUCT_TAG_DELETE_ALL_FAIL);
            }

            // 태그 리스트 하나씩 새로 Insert
            for (Integer tagId : patchProductReq.getTagIds()) {
                productDao.createProductTag(productId, tagId);
            }
            return "내 상품 수정에 성공하였습니다.";
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
