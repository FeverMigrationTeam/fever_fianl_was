package com.example.fever_final.table.payments.service;


import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.member.repository.MemberRepository;
import com.example.fever_final.table.payments.dto.AddPayInfoReqDto;
import com.example.fever_final.table.payments.dto.PayInfoRespDto;
import com.example.fever_final.table.payments.dto.UseTicketReqDto;
import com.example.fever_final.table.payments.entity.PayInfo;
import com.example.fever_final.table.payments.repository.PayRepository;
import com.example.fever_final.table.regala.entity.Regala;
import com.example.fever_final.table.regala.etc.RegalaStatus;
import com.example.fever_final.table.regala.repository.RegalaRepository;
import com.example.fever_final.table.ticket.dto.TicketAddReqDto;
import com.example.fever_final.table.ticket.dto.TicketListRespDto;
import com.example.fever_final.table.ticket.entity.Ticket;
import com.example.fever_final.table.ticket.etc.TicketType;
import com.example.fever_final.table.ticket.repository.TicketRepository;
import com.example.fever_final.util.http.HttpUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final Status status;
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final RegalaRepository regalaRepository;

    /* P1 : 이용권 추가 */
    @Transactional
    public ResponseEntity addTicket(@RequestBody TicketAddReqDto ticketAddReqDto) {

        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(ticketAddReqDto.getUserId());
        if (!byId.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_INVALID_MEMEBER_ID, new ResponseMessage().TICKET_INVALID_MEMEBER_ID
            ), HttpStatus.OK);
        }
        Member member = byId.get();
        Ticket ticket = new Ticket();

        //ticket type
        int type = ticketAddReqDto.getTicketType();

        ticket.setIsChecked(1);

        if (type == 0) {
            ticket.setTicketType(TicketType.SHORT);
        } else if (type == 1) {
            ticket.setTicketType(TicketType.MEDIUM);
        } else if (type == 2) {
            ticket.setTicketType(TicketType.LONG);
        } else {
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_INVALID_TYPE, new ResponseMessage().TICKET_INVALID_TYPE
            ), HttpStatus.OK);
        }

        // 양방향 매핑
        ticket.setMember(member);

        //ticket 저장
        ticketRepository.save(ticket);

        // member에 양방향
        member.setTicketList(ticket);

        return new ResponseEntity(NoDataResponse.response(
                status.SUCCESS, "이용권 추가 " + new ResponseMessage().SUCCESS
        ), HttpStatus.OK);

    }

    /* P2 : 이용권 사용 */
    @Transactional
    public ResponseEntity useTicket(UseTicketReqDto useTicketReqDto) {

        Long userId = useTicketReqDto.getUserId();
        Long ticketId = useTicketReqDto.getTicketId();
        Long regalaId = useTicketReqDto.getRegalaId();

        // userId 유효성 검사
        Optional<Member> byId1 = memberRepository.findById(userId);
        if (!byId1.isPresent())
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_INVALID_MEMEBER_ID, new ResponseMessage().TICKET_INVALID_MEMEBER_ID
            ), HttpStatus.OK);

        // ticketId 유효성 검사
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_REGALA_INVALID_ID, new ResponseMessage().TICKET_REGALA_INVALID_ID
            ), HttpStatus.OK);

        //regalaId 유효성 검사
        Optional<Regala> byId2 = regalaRepository.findById(regalaId);
        if (!byId2.isPresent())
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_INVALID_ID, new ResponseMessage().TICKET_INVALID_ID
            ), HttpStatus.OK);


        Ticket ticket = byId.get();


        // 이미 사용된 ticket일 때
        if (ticket.getIsChecked() == 0)
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_ALREADY_USED, new ResponseMessage().TICKET_ALREADY_USED
            ), HttpStatus.OK);

        // 리갈라 상태 확인
        Regala regala = byId2.get();
        if (regala.getRegalaStatus() == RegalaStatus.AVAILABLE) { // 사용가능

        } else if (regala.getRegalaStatus() == RegalaStatus.INUSE) { // 사용불가
            return new ResponseEntity(NoDataResponse.response(
                    status.TICEKT_REGALA_ALREADY_INUSE, new ResponseMessage().TICEKT_REGALA_ALREADY_INUSE
            ), HttpStatus.OK);
        }

        String stadiumName = regala.getStadium().getStadiumName();
        Map<String, Object> map = new HashMap<String, Object>();
        TicketType ticketType = ticket.getTicketType();
        map.put("userId", userId);
        map.put("regalaId", regalaId);
        map.put("stadiumName", stadiumName);
        map.put("ticketType", ticketType);

        String url = "http://localhost:5000/regala";

        String responseCode = HttpUtil.callApi(url, map, "POST");
        int code = Integer.parseInt(responseCode);
//        System.out.println("postRequest:" + response);

        if (code == 4506) // 리갈라 동작 불가 상태
            return new ResponseEntity(NoDataResponse.response(
                    status.TICEKT_REGALA_ALREADY_INUSE, new ResponseMessage().TICEKT_REGALA_ALREADY_INUSE
            ), HttpStatus.OK);
        else if (code == 4507) // ticketType 에러
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_REGALA_INVALID_TICKET_TYPE, new ResponseMessage().TICKET_REGALA_INVALID_TICKET_TYPE
            ), HttpStatus.OK);

        // 촬영 종료 시간
        SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        // 30분 더하기
        if (ticketType == TicketType.SHORT)
            cal.add(Calendar.MINUTE, 30);
        else if (ticketType == TicketType.MEDIUM)
            cal.add(Calendar.HOUR, 1);
        else if (ticketType == TicketType.LONG)
            cal.add(Calendar.HOUR, 2);
        else
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_REGALA_INVALID_TICKET_TYPE, new ResponseMessage().TICKET_REGALA_INVALID_TICKET_TYPE
            ), HttpStatus.OK);
        String time = sdformat.format(cal.getTime());
        String endTime = time.substring(time.length() - 8, time.length());

//        System.out.println("촬영종료 시간 : " + endTime); //05/13/2021 13:28:57


        // 리갈라 상태 변경
        regala.setRegalaStatus(RegalaStatus.INUSE); // 사용중

        // ticket Ischecked 변경 : 사용 불가능
        ticket.setIsChecked(0);

        ticketRepository.save(ticket);

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "이용권 사용 " + new ResponseMessage().SUCCESS, endTime
        ), HttpStatus.OK);

    }

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
                status.SUCCESS, "결제 clientId, secretId 불러오기 " + new ResponseMessage().SUCCESS, payInfoRespDto
        ), HttpStatus.OK);

    }


    /* P5 : 이용권 조회  */
    public ResponseEntity getTickets(Long userId, int isChecked) {


        // userId 유효성 검사
        Optional<Member> byId = memberRepository.findById(userId);
        if (!byId.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_INVALID_MEMBER_ID, new ResponseMessage().TICKET_INVALID_MEMBER_ID
            ), HttpStatus.OK);
        }

        Member member = byId.get();
        // 티켓 조회
        List<Ticket> allByMemberAndIsChecked = ticketRepository.findAllByMemberAndIsChecked(member, isChecked);
        List<TicketListRespDto> resultList = new ArrayList<>();
        TicketListRespDto ticketListRespDto = new TicketListRespDto();

        // dto 생성
        for (Ticket ticket : allByMemberAndIsChecked) {
            ticketListRespDto.setTicketId(ticket.getId());
            // 타입 검사
            if (ticket.getTicketType().equals(TicketType.SHORT)) {
                ticketListRespDto.setTicketName("30분 이용권");
                ticketListRespDto.setTicketType(0);
            } else if (ticket.getTicketType().equals(TicketType.MEDIUM)) {
                ticketListRespDto.setTicketName("1시간 이용권");
                ticketListRespDto.setTicketType(1);
            } else if (ticket.getTicketType().equals(TicketType.LONG)) {
                ticketListRespDto.setTicketName("2시간 이용권");
                ticketListRespDto.setTicketType(2);
            }

            // 리스트에 add
            resultList.add(ticketListRespDto);

        }

        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "이용권 조회 " + new ResponseMessage().SUCCESS, resultList
        ), HttpStatus.OK);


    }


}
