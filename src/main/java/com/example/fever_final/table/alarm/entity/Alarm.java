package com.example.fever_final.table.alarm.entity;


import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.alarm.dto.AlarmMakeReqDto;
import com.example.fever_final.table.alarm.etc.AlarmType;
import com.example.fever_final.table.member.dto.request.UserJoinDto;
import com.example.fever_final.table.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.websocket.server.ServerEndpoint;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alarm")
public class Alarm extends Timestamped {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_memeber_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private AlarmType alarmType;

    private String contents;

    private int isRead;

    public Alarm(Member member, String contents,AlarmType alarmType) {
        if (member == null)
            this.member = member;
        this.contents = contents;
        this.alarmType =alarmType;
        this.isRead = 0; // 안읽음.
    }



}
