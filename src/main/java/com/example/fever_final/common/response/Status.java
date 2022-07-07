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









}
