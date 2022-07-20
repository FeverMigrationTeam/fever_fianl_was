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
import com.example.fever_final.table.ticket.dto.TicketAddReqDto;
import com.example.fever_final.table.ticket.dto.TicketListRespDto;
import com.example.fever_final.table.ticket.entity.Ticket;
import com.example.fever_final.table.ticket.etc.TicketType;
import com.example.fever_final.table.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final Status status;
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;

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
    public ResponseEntity useTicket(Long ticketId) {

        // ticketId 유효성 검사
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        if (!byId.isPresent())
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_INVALID_ID, new ResponseMessage().TICKET_INVALID_ID
            ), HttpStatus.OK);

        Ticket ticket = byId.get();

        // member에서 삭제
//        Member member = ticket.getMember();
//        member.getTicketList().remove(ticket);
//        ArrayList<Ticket> tmpList = (ArrayList<Ticket>) member.getTicketList();

        // 이미 사용된 ticket일 때
        if (ticket.getIsChecked() == 0)
            return new ResponseEntity(NoDataResponse.response(
                    status.TICKET_ALREADY_USED, new ResponseMessage().TICKET_ALREADY_USED
            ), HttpStatus.OK);


        // ticket Ischecked 변경 : 사용 불가능
        ticket.setIsChecked(0);

        ticketRepository.save(ticket);

        return new ResponseEntity(NoDataResponse.response(
                status.SUCCESS, "이용권 사용 " + new ResponseMessage().SUCCESS
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
                status.SUCCESS, "이용권 조회 " + new ResponseMessage().SUCCESS,resultList
                ), HttpStatus.OK);


    }


}
