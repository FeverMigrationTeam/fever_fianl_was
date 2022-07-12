package com.example.fever_final.table.video.controller;


import com.example.fever_final.table.video.dto.MultipartDto;
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

    /* V1 : 비디오 업로드 */
    @PostMapping("/upload")
    public ResponseEntity uploadVideo(@ModelAttribute MultipartDto dto) {
        return videoService.uploadVideo(dto);
    }

    /* V2 : s3에서 영상 List 내려주기 */
    @GetMapping("/list/{userId}")
    public ResponseEntity getVideoList(@PathVariable Long userId) {
        return videoService.getVideoList(userId);
    }

    /* V3 : s3에서 영상 URl 내려주기 */
    @GetMapping("/{videoId}")
    public ResponseEntity getVideo(@PathVariable Long videoId){
        return videoService.getVideo(videoId);
    }

}
