package com.example.fever_final.table.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenReqDto {

        private Long userId;
        private String refreshToken;
} 