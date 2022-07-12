package com.example.fever_final.common.response;

import org.springframework.stereotype.Component;

@Component
public class Status {

    /*
     * 2000 success
     * 3XXX input error
     * 4XXX server error
     * 5XXX database error
     */

    public final int SUCCESS = 2000;

    public int EXISTED_NICKNAME = 3001;

    public final int EXPIRED_TOKEN = 4001;


    /* 4100 ~ : 유저관련 에러
    *
    * */


    // 로그인
    public final int LOGIN_INVALID_ID = 4100;
    public final int LOGIN_INVALID_PASSWORD = 4101;

    // 회원가입
    public final int SIGN_UP_INVALID_ID = 4102;
    public final int SIGN_UP_INVALID_PASSWORD = 4103;
    public final int SIGN_UP_ALREADY_EXISTED_ID = 4104;
    public final int SIGN_UP_INTERNAL_SERVER_ERROR = 4104;
    public final int MEMBER_INVALID_ID = 4105;

    /**
     * */
    // 유효성 검사
    public final int STADIUM_INVALID_NAME = 5001;
    public final int VIDEO_NULLPOINT_EXCEPTION = 5002;
}
