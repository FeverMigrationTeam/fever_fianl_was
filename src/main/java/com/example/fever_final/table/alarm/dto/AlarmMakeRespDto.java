package com.example.fever_final.table.alarm.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmMakeRespDto {

    private Long id;
    private String contents;
    private String createdAt;
}
