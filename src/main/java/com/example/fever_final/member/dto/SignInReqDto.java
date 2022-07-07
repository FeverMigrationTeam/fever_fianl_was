package com.example.fever_final.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInReqDto {

    private String phoneNumber;
    private String password;
}
