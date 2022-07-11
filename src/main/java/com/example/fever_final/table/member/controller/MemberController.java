package com.example.fever_final.table.member.controller;

import com.example.fever_final.table.member.dto.request.RefreshTokenReqDto;
import com.example.fever_final.table.member.dto.request.SignInReqDto;
import com.example.fever_final.table.member.dto.request.UserJoinDto;
import com.example.fever_final.table.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberController {

    private final MemberService signService;

    /* U1 : 이름 중복여부 확인(실명) */
    @GetMapping("/valid/{name}")
    public ResponseEntity validName(@PathVariable String name) {
        return signService.validName(name);
    }

    /* U2 : 회원가입 API */
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
        return signService.signUp(userJoinDto);
    }

    /* U3 : 일반로그인 API */
    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody SignInReqDto signInReqDto) {
        return signService.signIn(signInReqDto);
    }


    /* U4 : accessToken 재발급 API */
    @GetMapping("/reaccess")
    @ResponseBody
    public ResponseEntity getReaccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        return signService.getReaccessToken(refreshTokenReqDto);
    }


}
