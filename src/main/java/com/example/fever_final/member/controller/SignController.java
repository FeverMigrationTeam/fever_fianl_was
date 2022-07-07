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

    /* 이름 중복여부 확인(실명) */
    @PostMapping("test/validation/{name}")
    public ResponseEntity validname(@PathVariable String name){
        return signService.validname(name);
    }

    /* 일반로그인 API */
    @PostMapping("/signin")
//    @ResponseBody
    public SignResultRepDto signInUser(HttpServletRequest request, @RequestBody SignInReqDto signReqDto) {
        Member user = (Member) customUserDetailService.findByPhoneNumber(signReqDto.getPhoneNumber());
//        Member user = (Member) customUserDetailService.findByPhoneNumber(signReqDto.getPhoneNumber());

        SignResultRepDto signResultRepDto = new SignResultRepDto();

        try {
            if (passwordEncoder.matches(signReqDto.getPassword(), user.getPassword())) {
                System.out.println("비밀번호 일치");
//                List<String> roleList = Arrays.asList(user.getRoles().split(","));
                String roles = user.getRoles();
                signResultRepDto.setResult("success");
                signResultRepDto.setAccessToken(jwtTokenProvider.createToken(user.getPhoneNumber(), roles)); // access token 만들기
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


    @PostMapping("reaccess")
    @ResponseBody
    public ResponseEntity refreshAccessToken(HttpServletRequest request, @RequestBody RefreshTokenReqDto refreshTokenReqDto) {

        HashMap<String, String> tokens = new HashMap<>();

        Optional<Member> isExist = customUserDetailService.findById(refreshTokenReqDto.getUserId());
        if (isExist.isPresent()) { // 해당 유저 존재해야됨
            Member user = isExist.get();
            String userRefreshToken = user.getRefreshToken();
            String reqToken = refreshTokenReqDto.getRefreshToken();
            if (userRefreshToken != null && userRefreshToken.equals(reqToken)) { // refreshToken 유효해야
//                List<String> roleList = Arrays.asList(user.getRoles().split(","));
                String roles = user.getRoles();
                tokens.put("accesstoken", jwtTokenProvider.createToken(user.getPhoneNumber(), roles));
                userRefreshToken = jwtTokenProvider.createRefreshToken(user.getPhoneNumber());
                tokens.put("refreshtoken", userRefreshToken);
                user.setRefreshToken(userRefreshToken); // refreshToken 업데이트

                customUserDetailService.save(user); // 새롭게 refreshToken 업데이트 된 User DB에 업데이트
                return new ResponseEntity(DataResponse.response(status.SUCCESS,
                        "access token 재발급 " + message.SUCCESS, tokens), HttpStatus.OK);

            }
        }

        return new ResponseEntity(NoDataResponse.response(status.EXPIRED_TOKEN, message.EXPIRED_TOKEN), HttpStatus.OK);
    }
}
