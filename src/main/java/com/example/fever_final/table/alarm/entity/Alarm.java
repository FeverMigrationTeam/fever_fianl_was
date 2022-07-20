package com.example.fever_final.table.alarm.entity;


import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.alarm.dto.AlarmMakeReqDto;
import com.example.fever_final.table.alarm.etc.AlarmType;
import com.example.fever_final.table.member.dto.request.UserJoinDto;
import com.example.fever_final.table.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import javax.websocket.server.ServerEndpoint;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "alarm")
public class Alarm extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_memeber_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private AlarmType alarmType;

    private String contents;

    private int isRead;

    public static Alarm buildAlarm(Member member, String contents, AlarmType alarmType) {
        return Alarm.builder()
                .member(member)
                .contents(contents)
                .alarmType(alarmType)
                .isRead(0)
                .build();
    }



}
