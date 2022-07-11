package com.example.fever_final.table.video.controller;


import com.example.fever_final.table.video.dto.UploadReqDto;
import com.example.fever_final.table.video.service.VideoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {


    private final VideoService videoService;

    /* 비디오 업로드 */
    @PostMapping("/upload")
    public ResponseEntity uploadVideo(@RequestPart MultipartFile video,
                                      @RequestPart UploadReqDto uploadReqDto) {
        return videoService.uploadVideo(video,uploadReqDto);
    }



}
