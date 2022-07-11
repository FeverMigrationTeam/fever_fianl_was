package com.example.fever_final.util.s3.compent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ConfigurationProperties(prefix = "cloud.aws.s3") // yml 확장자 파일 읽는 어노테이션
@Component // 직접 작성한 Class를 Bean 으로 등록하기 위한 어노테이션
public class S3VideoComponent {

    private String bucket;
}
