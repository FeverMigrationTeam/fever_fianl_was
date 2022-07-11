package com.example.fever_final.util.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.example.fever_final.util.s3.component.S3VideoComponent;
import com.example.fever_final.util.IUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AwsS3UploadImpl implements IUpload {

    private final AmazonS3 amazonS3;
    private final S3VideoComponent component; // aws s3 bucket과 연결 ? 해주는 컴포넌트

    // cloudFront 배포도메인
    public final static String CLOUD_FRONT_DOMAIN_NAME = "https://d33ipfyyudd4bi.cloudfront.net";

    @Override
    public String uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(
            new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return getFileUrl(fileName);
    }

    @Override
    public String getFileUrl(String fileName) {
        String s = amazonS3.getUrl(component.getBucket(), fileName).toString();
        return s;
    }

}