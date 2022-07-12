package com.example.fever_final.common.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {
    /*
     * Set same variable name class Status's variable name
     */

    public String SUCCESS = "성공";

    // 유효성 검사
    public String MEMBER_INVALID_ID = "존재하지 않는 유저입니다.";
    public String STADIUM_INVALID_NAME = "존재하지 않는 경기장입니다.";
    public String REGALA_INVALID_ID = "존재하지 않는 리갈라 id입니다.";
    public String REGALA_INVALID_STATUS = "존재하지 않는 리갈라 상태코드입니다. int : 1(사용가능) 혹은 0(사용불가)로 입력해주세요.";

    // 로그인 회원가입 관련
    public String EXPIRED_TOKEN = "REFRESH TOKEN이 만료되었습니다. 새롭게 로그인 해주세요.";
    public String EXISTED_NICKNAME = "이미 닉네임이 존재합니다. 다른 닉네임을 작성해 주세요.";
    public String LOGIN_INVALID_ID = "존재하지 않는 전화번호(ID)입니다.";
    public String LOGIN_INVALID_PASSWORD = "password가 틀렸습니다.";
    public String SIGN_UP_INVALID_ID = "전화번호는 (-)를 제외한 11자입니다.";
    public String SIGN_UP_ALREADY_EXISTED_ID = "이미 존재하는 전화번호 입니다.";
    public String SIGN_UP_INTERNAL_SERVER_ERROR = "알수 없는 예외입니다.";
    public String SIGN_UP_INVALID_PASSWORD = "비밀번호는 3자이상 입력해주세요.";

    // video 관련
    public String VIDEO_NULLPOINT_EXCEPTION = "비디오가 없습니다.비디오를 첨부해주세요.";




}
