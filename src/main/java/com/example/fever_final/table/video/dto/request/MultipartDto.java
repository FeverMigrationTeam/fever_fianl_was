package com.example.fever_final.table.video.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipartDto {

    private MultipartFile video;
    private Long userId;
    private Long regalaId;
    private String stadiumName;

}
