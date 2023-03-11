package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final JwtService jwtService;
    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final S3Uploader s3Uploader;


    public PostProductRes createProduct(PostProductReq postProductReq, List<MultipartFile> images) throws BaseException {
        try {
            int lastInsertId = productDao.createProduct(postProductReq);
            List<String> imageUrl = s3Uploader.uploadFile(images, "items/"+lastInsertId, lastInsertId);
            List<String> resultUrl = new ArrayList<>();
            for (String image : imageUrl) {
                if (image == null) break;
                resultUrl.add(image);
                productDao.createProductImage(lastInsertId, image);
            }
            for (Integer tagId : postProductReq.getTagIds()) {
                productDao.createProductTag(lastInsertId, tagId);
            }
            return new PostProductRes(lastInsertId, resultUrl);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
