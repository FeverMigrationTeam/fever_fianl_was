package com.example.fever_final.table.video.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.member.repository.MemberRepository;
import com.example.fever_final.table.regala.entity.Regala;
import com.example.fever_final.table.regala.etc.RegalaStatus;
import com.example.fever_final.table.regala.repository.RegalaRepository;
import com.example.fever_final.table.stadium.entity.Stadium;
import com.example.fever_final.table.stadium.repository.StadiumRepository;
import com.example.fever_final.table.video.dto.MultipartDto;
import com.example.fever_final.table.video.entity.Video;
import com.example.fever_final.table.video.repository.VideoRepository;
import com.example.fever_final.util.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final Status status;
    private final StadiumRepository stadiumRepository;
    private final S3Service s3Service;
    private final RegalaRepository regalaRepository;

    /* 비디오 업로드 */
    @Transactional
    public ResponseEntity uploadVideo(MultipartDto dto) {
        MultipartFile video = dto.getVideo();

        // video null 유효성
        if(video == null)
            return new ResponseEntity(NoDataResponse.response(status.VIDEO_NULLPOINT_EXCEPTION
                    , new ResponseMessage().VIDEO_NULLPOINT_EXCEPTION
            ), HttpStatus.NOT_FOUND);


        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(dto.getUserId());
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.MEMBER_INVALID_ID
                    , new ResponseMessage().MEMBER_INVALID_ID
            ), HttpStatus.NOT_FOUND);

        // 유저 생성
        Member member = byId.get();

        // stadium 유효성 검사
        Optional<Stadium> byStadiumName = stadiumRepository.findByStadiumName(dto.getStadiumName());
        if (!byStadiumName.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.STADIUM_INVALID_NAME
                    , new ResponseMessage().STADIUM_INVALID_NAME
            ), HttpStatus.NOT_FOUND);

        Stadium stadium = byStadiumName.get();
        String imgUrl = s3Service.uploadS3(video);

        //regala 유효성 검사
        Optional<Regala> byId1 = regalaRepository.findById(dto.getRegalaId());
        if(!byId1.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.REGALA_INVALID_ID
                    , new ResponseMessage().REGALA_INVALID_ID
            ), HttpStatus.NOT_FOUND);

        Regala regala = byId1.get();

        // Video 빌드
        Video video1 = Video.buildUpload(imgUrl, member, stadium,regala);
        // db에 저장
        videoRepository.save(video1);

        // 리갈라 상태 값 변경 : 사용가능
        if(regala.getRegalaStatus().equals(RegalaStatus.INUSE))
            regala.setRegalaStatus(RegalaStatus.AVAILABLE);


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "비디오 업로드 " + new ResponseMessage().SUCCESS, imgUrl
        ), HttpStatus.OK);

    }


    /* V2 : s3에서 영상 List 내려주기 */
    public ResponseEntity getVideoList( Long userId){

    }
}
