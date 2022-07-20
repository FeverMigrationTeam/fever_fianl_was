package com.example.fever_final.table.payments.controller;


import com.example.fever_final.table.payments.dto.AddPayInfoReqDto;
import com.example.fever_final.table.payments.service.PayService;
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


}
