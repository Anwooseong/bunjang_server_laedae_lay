package com.example.demo.src.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private int cnt = 0;

    public List<String> uploadFile(List<MultipartFile> multipartFiles, String dirName, int productId) {
        List<String> fileUrlList = new ArrayList<>(Arrays.asList(null, null, null, null, null));

        for (int i = 0; i < 5; i++) {
            deleteFile(dirName + "/" + productId + "-" + i + ".jpg");
        }
        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 S3에 업로드하고 fileUrlList에 추가하여 리턴
        // MultipartFile 을 따로 File로 만드는 것이 아닌 InputStream 을 받는 방식
        multipartFiles.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename(), dirName, productId, cnt);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            fileUrlList.add(cnt,amazonS3Client.getUrl(bucket, fileName).toString()); //업로드한 파일 URL 저장
            cnt++;
        });
        cnt = 0;
        return fileUrlList;
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String uploadFile, String dirName, int productId, int cnt) {
        String count = String.valueOf(cnt);
        String fileName = dirName + "/" + productId + "-" + count + ".jpg";

        return fileName;
    }
}
