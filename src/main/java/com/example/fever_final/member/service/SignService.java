package com.example.fever_final.member.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.member.entity.Member;
import com.example.fever_final.member.repository.OuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignService {

    private final OuthRepository outhRepository;
    private final Status status;


    /* 닉네임 중복여부 확인(유저 닉네임) */
    public ResponseEntity validname(String nickname){
        Optional<Member> isExist = outhRepository.findByName(nickname);

        if(isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.EXISTED_NICKNAME
                    ,   new ResponseMessage().EXISTED_NICKNAME
            ), HttpStatus.NOT_FOUND);


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, nickname +" 닉네임  " + new ResponseMessage().SUCCESS, nickname
        ), HttpStatus.OK);
    }

}
