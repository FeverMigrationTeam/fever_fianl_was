package com.example.fever_final.table.payments.controller;


import com.example.fever_final.table.payments.dto.AddPayInfoReqDto;
import com.example.fever_final.table.payments.dto.UseTicketReqDto;
import com.example.fever_final.table.payments.service.PayService;
import com.example.fever_final.table.ticket.dto.TicketAddReqDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PayController {

    private final PayService payService;

    /* P1 : 이용권 추가 */
    @PostMapping("/ticket")
    public ResponseEntity addTicket(@RequestBody TicketAddReqDto TicketAddReqDto) {
        return payService.addTicket(TicketAddReqDto);
    }

    /* P2 : 이용권 사용 */
    @PatchMapping("/ticket")
    public ResponseEntity useTicket(@RequestBody UseTicketReqDto useTicketReqDto) {
        return payService.useTicket(useTicketReqDto);
    }


    /* P3 : 결제 clientId, secretId 저장 */
    @PostMapping("/info")
    public ResponseEntity addPayInfo(@RequestBody AddPayInfoReqDto addPayInfoReqDto) {
        return payService.addPayInfo(addPayInfoReqDto);

    }

    /* P4 : 결제 clientId, secretId 불러오기 */
    @GetMapping("/info/{userId}")
    public ResponseEntity getPayInfo(@PathVariable Long userId) {
        return payService.getPayInfo(userId);
    }

    /* P5 : 이용권 조회  */
    @GetMapping("/ticket/{userId}/{isChecked}")
    public ResponseEntity getTickets(@PathVariable Long userId, @PathVariable int isChecked) {
        return payService.getTickets(userId, isChecked);
    }


}
