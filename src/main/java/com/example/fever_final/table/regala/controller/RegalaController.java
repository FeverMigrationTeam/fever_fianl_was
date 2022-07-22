package com.example.fever_final.table.regala.controller;


import com.example.fever_final.table.regala.dto.RegalaStatusReqDto;
import com.example.fever_final.table.regala.service.RegalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regala")
public class RegalaController {

    private final RegalaService regalaService;


    /* 리갈라 상태확인 */
    @GetMapping("/status/{regalaId}")
    public ResponseEntity getStatus(@PathVariable Long regalaId) {
        return regalaService.getStatus(regalaId);
    }

    /* 리갈라 상태변경 */
    @PatchMapping("/status/update")
    public ResponseEntity updateStatus(@RequestBody RegalaStatusReqDto dto) {
        return regalaService.updateStatus(dto);
    }
}
