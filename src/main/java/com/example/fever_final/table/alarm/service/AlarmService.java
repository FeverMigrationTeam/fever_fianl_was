package com.example.fever_final.table.alarm.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.table.alarm.dto.AlarmMakeReqDto;
import com.example.fever_final.table.alarm.dto.AlarmMakeRespDto;
import com.example.fever_final.table.alarm.entity.Alarm;
import com.example.fever_final.table.alarm.etc.AlarmType;
import com.example.fever_final.table.alarm.repository.AlarmRepository;
import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AlarmService {


    private final Status status;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    /* A1 : 알람 목록 조회하기 */
    public ResponseEntity getAlarms(Long userId) {
        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(userId);
        if (!byId.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(
                    status.ALARM_INVALID_MEMBER_ID, new ResponseMessage().ALARM_INVALID_MEMBER_ID
            ), HttpStatus.OK);
        }

        Member member = byId.get();
        List<Alarm> allByMember = alarmRepository.findAllByMember(member);


        // db no data
        if (allByMember.isEmpty())
            return new ResponseEntity(NoDataResponse.response(
                    status.ALARM_NO_DATA, new ResponseMessage().ALARM_NO_DATA
            ), HttpStatus.OK);

        // dto 변환
        List<AlarmMakeRespDto> list = new ArrayList<>();
        for (Alarm alarm : allByMember) {
            AlarmMakeRespDto alarmMakeRespDto
                    = new AlarmMakeRespDto(alarm.getId(),
                    alarm.getContents(),
                    alarm.getCreatedAt().toString().substring(0, 10));
            list.add(alarmMakeRespDto);
        }

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "알람 목록 조회하기 " + new ResponseMessage().SUCCESS, list
        ), HttpStatus.OK);

    }


    /* A2 : 알람 생성 */
    @Transactional
    public ResponseEntity makeAlarm(@RequestBody AlarmMakeReqDto alarmMakeReqDto) {

        Long userId = alarmMakeReqDto.getUserId();
        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(userId);
        if (!byId.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(
                    status.ALARM_INVALID_MEMBER_ID, new ResponseMessage().ALARM_INVALID_MEMBER_ID
            ), HttpStatus.OK);
        }

        String contents = alarmMakeReqDto.getContents();
        String resultContents;
        AlarmType alarmType;

        if (contents.equals("결제")) {
            resultContents = "결제가 완료되었습니다.";
            alarmType = AlarmType.PAYMENT;
        } else if (contents.equals("촬영완료")) {
            resultContents = "촬영이 완료되었습니다.";
            alarmType = AlarmType.VIDEO;
        } else if (contents.equals("공유완료")) {
            resultContents = "영상이 공유되었습니다.";
            alarmType = AlarmType.VIDEO;
        } else {
            resultContents = null;
            return new ResponseEntity(NoDataResponse.response(
                    status.ALARM_INVALID_CONTENT_TYPE, new ResponseMessage().ALARM_INVALID_CONTENT_TYPE
            ), HttpStatus.OK);
        }


        // 알람 생성
        Alarm alarm = new Alarm(byId.get(), resultContents, alarmType);
        alarmRepository.save(alarm);

        AlarmMakeRespDto alarmMakeRespDto
                = new AlarmMakeRespDto(alarm.getId(),
                alarm.getContents(),
                alarm.getCreatedAt().toString().substring(0, 10));


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "알람 생성 " + new ResponseMessage().SUCCESS, alarmMakeRespDto
        ), HttpStatus.OK);
    }

    /* A3 : 알람 상태 읽음 처리 */
    @Transactional
    public ResponseEntity readAlarm(Long alarmId) {

        //alarmId 유효성 검사
        Optional<Alarm> byId = alarmRepository.findById(alarmId);
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(
                    status.ALARM_INVALID_ALARM_ID, new ResponseMessage().ALARM_INVALID_ALARM_ID
            ), HttpStatus.OK);

        Alarm alarm = byId.get();

        // isRead : 1 으로 읽음 처리
        if (alarm.getIsRead() == 0)
            alarm.setIsRead(1);

        alarmRepository.save(alarm);

        return new ResponseEntity(NoDataResponse.response(
                status.SUCCESS, "알람 읽음 처리 " + new ResponseMessage().SUCCESS
        ), HttpStatus.OK);
    }

}
