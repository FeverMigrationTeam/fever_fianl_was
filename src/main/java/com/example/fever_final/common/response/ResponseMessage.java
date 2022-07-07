package com.example.fever_final.common.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {
    /*
     * Set same variable name class Status's variable name
     */

    public final String SUCCESS = "성공";
    public final String EXPIRED_TOKEN = "REFRESH TOKEN이 만료되었습니다. 새롭게 로그인 해주세요.";
    public final String EXISTED_NICKNAME = "이미 닉네임이 존재합니다. 다른 닉네임을 작성해 주세요.";
    public final String LOGIN_INVALID_ID = "존재하지 않는 전화번호(ID)입니다.";
    public final String LOGIN_INVALID_PASSWORD = "password가 틀렸습니다.";


}
