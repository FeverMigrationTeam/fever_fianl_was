package com.example.fever_final.util.s3.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.fever_final.util.IUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final IUpload s3Service;

    /* s3에 영상 업로드 */
    public String uploadS3(MultipartFile video){
        String fileName = createFileName(video.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(video.getContentType());
        objectMetadata.setContentLength(video.getSize());
        try (InputStream inputStream = video.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", video.getOriginalFilename()));
        }
        String imageUrl = s3Service.getFileUrl(fileName);
        int start = imageUrl.indexOf("com");
        int end = imageUrl.length();
        String cdnImageUrl = AwsS3UploadImpl.CLOUD_FRONT_DOMAIN_NAME + imageUrl.substring(start + 4, end);

        return cdnImageUrl;
    }

    // 유니크한 파일이름 생성하는 로직
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    // 파일 확장자 가져오는 로직
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }
    }
}
