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

    /* 4200 ~ : video 관련
    *
    * */

    public final int VIDEO_MEMBER_NO_VIDEOS = 4200;
    public final int VIDEO_INVALID_ID = 4201;


    /* 4300 ~ : alarm 관련
    *
    * */
    public final int ALARM_NO_DATA = 4300;
    public final int ALARM_INVALID_MEMBER_ID = 4301;
    public final int ALARM_INVALID_CONTENT_TYPE = 4302;
    public final int ALARM_INVALID_ALARM_ID = 4303;

    /* 4400 ~ : pay 관련
    *
    * */
    public final int PAY_INVALID_MEMEBER_ID = 4400;
    public final int PAY_NO_DATA = 4401;
    public final int PAY_ALREADY_EXISTED = 4402;


    // 유효성 검사
    public final int STADIUM_INVALID_NAME = 5001;
    public final int VIDEO_NULLPOINT_EXCEPTION = 5002;
    public final int REGALA_INVALID_ID = 5003;
    public final int REGALA_INVALID_STATUS = 5004;


}
