package com.example.fever_final.table.video.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfoReqDto {

    private String url;
    private Long userId;
    private Long regalaId;
    private String stadiumName;

}
