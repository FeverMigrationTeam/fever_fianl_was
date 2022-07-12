package com.example.fever_final.table.video.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.member.repository.MemberRepository;
import com.example.fever_final.table.stadium.Stadium;
import com.example.fever_final.table.stadium.repository.StadiumRepository;
import com.example.fever_final.table.video.dto.UploadReqDto;
import com.example.fever_final.table.video.entity.Video;
import com.example.fever_final.table.video.repository.VideoRepository;
import com.example.fever_final.util.s3.service.AwsS3UploadImpl;
import com.example.fever_final.util.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final Status status;
    private final ResponseMessage message;
    private final StadiumRepository stadiumRepository;
    private final S3Service s3Service;

    /* 비디오 업로드 */
    @Transactional
    public ResponseEntity uploadVideo(MultipartFile video, UploadReqDto uploadReqDto) {

        // video null 유효성
        if(video == null)
            return new ResponseEntity(NoDataResponse.response(status.VIDEO_NULLPOINT_EXCEPTION
                    , new ResponseMessage().VIDEO_NULLPOINT_EXCEPTION
            ), HttpStatus.NOT_FOUND);


        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(uploadReqDto.getUserId());
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.MEMBER_INVALID_ID
                    , new ResponseMessage().MEMBER_INVALID_ID
            ), HttpStatus.NOT_FOUND);

        // 유저 생성
        Member member = byId.get();

        // stadium 유효성 검사
        Optional<Stadium> byStadiumName = stadiumRepository.findByStadiumName(uploadReqDto.getStadiumName());
        if (!byStadiumName.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.STADIUM_INVALID_NAME
                    , new ResponseMessage().STADIUM_INVALID_NAME
            ), HttpStatus.NOT_FOUND);

        Stadium stadium = byStadiumName.get();
        String imgUrl = s3Service.uploadS3(video);
        // Video 빌드
        Video video1 = Video.buildUpload(imgUrl, member, stadium);
        // db에 저장
        videoRepository.save(video1);

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "비디오 업로드 " + new ResponseMessage().SUCCESS, imgUrl
        ), HttpStatus.OK);

    }
}
