package com.example.fever_final.member.controller;

import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.config.CommonEncoder;
import com.example.fever_final.config.security.JwtTokenProvider;
import com.example.fever_final.member.dto.SignInReqDto;
import com.example.fever_final.member.dto.SignResultRepDto;
import com.example.fever_final.member.dto.UserJoinDto;
import com.example.fever_final.member.entity.Member;
import com.example.fever_final.member.service.CustomMemeberDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/start")
public class SignController {
    private final CustomMemeberDetailService customUserDetailService;

    private final CommonEncoder passwordEncoder = new CommonEncoder();

    private final JwtTokenProvider jwtTokenProvider;
    private final Status status;
    private final ResponseMessage message;

    /* 일반로그인 API */
    @PostMapping("/signin")
    @ResponseBody
    public SignResultRepDto signInUser(HttpServletRequest request, @RequestBody SignInReqDto signReqDto) {
        Member user = (Member) customUserDetailService.findByPhoneNumber(signReqDto.getPhoneNumber());
        SignResultRepDto signResultRepDto = new SignResultRepDto();

        try {
            if (passwordEncoder.matches(signReqDto.getPassword(), user.getPassword())) {
                System.out.println("비밀번호 일치");
                List<String> roleList = Arrays.asList(user.getRoles().split(","));
                signResultRepDto.setResult("success");
                signResultRepDto.setAccessToken(jwtTokenProvider.createToken(user.getPhoneNumber(), roleList)); // access token 만들기
                String tmpRefreshToken = jwtTokenProvider.createRefreshToken(user.getPhoneNumber());
                signResultRepDto.setRefreshToken(tmpRefreshToken); // refresh token 만들기

                // 새롭게 DB에 refresh token 변경
                user.setRefreshToken(tmpRefreshToken);
                customUserDetailService.save(user);

                return signResultRepDto;
            } else {
                signResultRepDto.setResult("fail");
                signResultRepDto.setMessage(" ID or Password is invalid.");
                return signResultRepDto;
            }
        } catch (NullPointerException e) {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("NullPointerException");
            return signResultRepDto;
        }
    }

    /* 회원가입 API */
    @PostMapping("/signup")
    @ResponseBody
    public SignResultRepDto addUser(HttpServletRequest request, @RequestBody UserJoinDto userJoinDto) {
        Member user = new Member(userJoinDto);

        user.setRoles("USER"); // 역할(권한) 부여
        user.setPassword(passwordEncoder.encode(userJoinDto.getPassword())); // 패스워드 암호화
        SignResultRepDto signResultRepDto = new SignResultRepDto();
        int result = customUserDetailService.signUpUser(user);
        if (result == 1) {
            signResultRepDto.setResult("success");
            signResultRepDto.setMessage("회원 가입 성공 !");
            return signResultRepDto;
        } else if (result == -1) {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("전화번호가 존재합니다. 비밀번호 찾기를 진행해주세요.");
            return signResultRepDto;
        } else {
            signResultRepDto.setResult("fail");
            signResultRepDto.setMessage("Ask system admin");
            return signResultRepDto;
        }

    }
}
