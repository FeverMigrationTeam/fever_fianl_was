package com.example.fever_final.util.s3.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadReqDto {

    private Long userId; // 촬영권을 소모한 유저 id
    private String regalaId; // 리갈라 고유 id
    private MultipartFile video; // 비디오

}
