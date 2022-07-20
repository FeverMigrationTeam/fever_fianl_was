package com.example.fever_final.table.payments.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.member.repository.MemberRepository;
import com.example.fever_final.table.payments.dto.AddPayInfoReqDto;
import com.example.fever_final.table.payments.dto.PayInfoRespDto;
import com.example.fever_final.table.payments.entity.PayInfo;
import com.example.fever_final.table.payments.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final Status status;
    private final MemberRepository memberRepository;

    /* P3 : 결제 clientId, secretId 저장 */
    @Transactional
    public ResponseEntity addPayInfo(AddPayInfoReqDto addPayInfoReqDto) {

        Long userId = addPayInfoReqDto.getUserId();
        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(userId);
        if (!byId.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(
                    status.PAY_INVALID_MEMEBER_ID, new ResponseMessage().PAY_INVALID_MEMEBER_ID
            ), HttpStatus.OK);
        }

        Member member = byId.get();

        // payinfo 존재유무 검사
        Optional<PayInfo> byUserId = payRepository.findByUserId(userId);
        if (byUserId.isPresent()) {

            PayInfo payInfo = byUserId.get();

            payInfo.setClientId(addPayInfoReqDto.getClientId());
            payInfo.setSecretId(addPayInfoReqDto.getSecretId());

            // db반영
            payRepository.save(payInfo);
            member.setPayInfo(payInfo);
            memberRepository.save(member);

            return new ResponseEntity(NoDataResponse.response(
                    status.PAY_ALREADY_EXISTED, new ResponseMessage().PAY_ALREADY_EXISTED
            ), HttpStatus.OK);
        }


        // pay 생성
        PayInfo payInfo = PayInfo.buildPayInfo(
                userId,
                addPayInfoReqDto.getClientId(),
                addPayInfoReqDto.getSecretId());


        // db반영
        payRepository.save(payInfo);
        member.setPayInfo(payInfo);
        memberRepository.save(member);



        return new ResponseEntity(NoDataResponse.response(
                status.SUCCESS, "결제 clientId, secretId 저장 " + new ResponseMessage().SUCCESS
        ), HttpStatus.OK);

    }


    /* P4 : 결제 clientId, secretId 불러오기 */
    public ResponseEntity getPayInfo(@PathVariable Long userId) {

        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(userId);
        if (!byId.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(
                    status.PAY_INVALID_MEMEBER_ID, new ResponseMessage().PAY_INVALID_MEMEBER_ID
            ), HttpStatus.OK);
        }

        // 불러오기
        Optional<PayInfo> byUserId = payRepository.findByUserId(userId);
        if (!byUserId.isPresent())
            return new ResponseEntity(NoDataResponse.response(
                    status.PAY_NO_DATA, new ResponseMessage().PAY_NO_DATA
            ), HttpStatus.OK);

        PayInfo payInfo = byUserId.get();

        PayInfoRespDto payInfoRespDto = new PayInfoRespDto(payInfo.getClientId(), payInfo.getSecretId());

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "결제 clientId, secretId 불러오기 " + new ResponseMessage().SUCCESS,payInfoRespDto
        ), HttpStatus.OK);

    }

}
