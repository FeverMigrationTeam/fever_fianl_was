package com.example.fever_final.table.regala.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.table.regala.dto.RegalaStatusReqDto;
import com.example.fever_final.table.regala.entity.Regala;
import com.example.fever_final.table.regala.etc.RegalaStatus;
import com.example.fever_final.table.regala.repository.RegalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegalaService {

    private final Status status;
    private final RegalaRepository regalaRepository;


    /* 리갈라 상태확인 */
    public ResponseEntity getStatus(Long regalaId) {

        // 리갈라 id 유효성 검사
        Optional<Regala> byId = regalaRepository.findById(regalaId);
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.REGALA_INVALID_ID
                    , new ResponseMessage().REGALA_INVALID_ID
            ), HttpStatus.NOT_FOUND);

        Regala regala = byId.get();

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "리갈라상태값 조회" + new ResponseMessage().SUCCESS, regala.getRegalaStatus()
        ), HttpStatus.OK);

    }


    /* 리갈라 상태변경 */
    public ResponseEntity updateStatus( RegalaStatusReqDto dto){

        // 리갈라 id 유효성 검사
        Optional<Regala> byId = regalaRepository.findById(dto.getRegalaId());
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.REGALA_INVALID_ID
                    , new ResponseMessage().REGALA_INVALID_ID
            ), HttpStatus.NOT_FOUND);

        Regala regala = byId.get();

        if (dto.getStatus() == 1) // 사용가능으로 변경
            regala.setRegalaStatus(RegalaStatus.AVAILABLE); // 변경
        else if(dto.getStatus() == 0) // 사용불가로 변경
            regala.setRegalaStatus(RegalaStatus.INUSE); //
        else
            return new ResponseEntity(NoDataResponse.response(status.REGALA_INVALID_STATUS
                    , new ResponseMessage().REGALA_INVALID_STATUS
            ), HttpStatus.NOT_FOUND);


        regalaRepository.save(regala);

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "리갈라상태값 변경" + new ResponseMessage().SUCCESS, regala.getRegalaStatus()
        ), HttpStatus.OK);

    }


    /******
     *
     *
     *
     * ******/




}
