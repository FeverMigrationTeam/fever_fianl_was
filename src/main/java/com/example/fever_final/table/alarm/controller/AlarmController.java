package com.example.fever_final.table.alarm.controller;


import com.example.fever_final.table.alarm.dto.AlarmMakeReqDto;
import com.example.fever_final.table.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {


    private final AlarmService alarmService;

    /* A1 : 알람 목록 불러오기 */
    @GetMapping("/list/{userId}")
    public ResponseEntity getAlarms(@PathVariable Long userId) {
        return alarmService.getAlarms(userId);
    }

    /* A2 : 알람 생성 */
    @PostMapping("")
    public ResponseEntity makeAlarm(@RequestBody AlarmMakeReqDto alarmMakeReqDto){
        return alarmService.makeAlarm(alarmMakeReqDto);
    }

    @PatchMapping("/read/{alarmId}")
    public ResponseEntity readAlarm(@PathVariable Long alarmId){
        return alarmService.readAlarm(alarmId);
    }
}
