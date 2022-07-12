package com.example.fever_final.table.regala.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegalaStatusReqDto {

    private Long regalaId;
    private int status; // 1 : 사용가능 , 0 : 사용불가
}
