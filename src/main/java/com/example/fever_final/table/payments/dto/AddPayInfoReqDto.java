package com.example.fever_final.table.payments.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPayInfoReqDto {
    private Long userId;
    private String clientId;
    private String secretId;

}
