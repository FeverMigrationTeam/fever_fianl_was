package com.example.fever_final.table.video.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoListRespDto {

    private Long id;
    private String createdAt;
    private String url;
    private String stadiumName;
}
