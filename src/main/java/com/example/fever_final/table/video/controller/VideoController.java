package com.example.fever_final.table.video.controller;


import com.example.fever_final.table.video.dto.request.MultipartDto;
import com.example.fever_final.table.video.dto.request.VideoInfoReqDto;
import com.example.fever_final.table.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {


    private final VideoService videoService;

    /* V1 : 비디오 업로드 : dto 버전*/
    @PostMapping("/upload/v1")
    public ResponseEntity uploadVideoWithDto(@ModelAttribute MultipartDto dto) {
        return videoService.uploadVideoWithDto(dto);
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

    /* V4 : s3 영상 업로드 : s3 비디오 단독 업로드 */
    @PostMapping("/upload")
    public ResponseEntity uploadVideo(@RequestParam MultipartFile video) {
        return videoService.uploadVideo(video);
    }

    /* V5 : s3 영상 url, 정보 db저장 */
    @PostMapping("/upload/info")
    public ResponseEntity uploadVideoWithInfo(@RequestBody VideoInfoReqDto dto) {
        return videoService.uploadVideoWithInfo(dto);
    }


}
