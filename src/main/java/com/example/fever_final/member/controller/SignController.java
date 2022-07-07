package com.example.fever_final.member.controller;

import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.common.CommonEncoder;
import com.example.fever_final.config.security.JwtTokenProvider;
import com.example.fever_final.member.dto.RefreshTokenReqDto;
import com.example.fever_final.member.dto.SignInReqDto;
import com.example.fever_final.member.dto.SignResultRepDto;
import com.example.fever_final.member.dto.UserJoinDto;
import com.example.fever_final.member.entity.Member;
import com.example.fever_final.member.service.CustomMemeberDetailService;
import com.example.fever_final.member.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/start")
public class SignController {
    private final CustomMemeberDetailService customUserDetailService;

    private final CommonEncoder passwordEncoder = new CommonEncoder();
    private final JwtTokenProvider jwtTokenProvider;
    private final Status status;
    private final ResponseMessage message;
    private final SignService signService;

    /* U1 : 이름 중복여부 확인(실명) */
    @PostMapping("/signup/valid/{name}")
    public ResponseEntity validname(@PathVariable String name) {
        return signService.validname(name);
    }

    /* U2 : 회원가입 API */
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserJoinDto userJoinDto) {
        return signService.signUp(userJoinDto);
    }

    /* U3 : 일반로그인 API */
    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody SignInReqDto signInReqDto) {
        return signService.signIn(signInReqDto);
    }


    /* U4 : accessToken 재발급 API */
    @PostMapping("/reaccess")
    @ResponseBody
    public ResponseEntity getReaccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        return signService.getReaccessToken(refreshTokenReqDto);
    }


}
