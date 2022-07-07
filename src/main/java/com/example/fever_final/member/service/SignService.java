package com.example.fever_final.member.service;


import com.example.fever_final.common.CommonEncoder;
import com.example.fever_final.common.response.DataResponse;
import com.example.fever_final.common.response.NoDataResponse;
import com.example.fever_final.common.response.ResponseMessage;
import com.example.fever_final.common.response.Status;
import com.example.fever_final.config.security.JwtTokenProvider;
import com.example.fever_final.member.dto.RefreshTokenReqDto;
import com.example.fever_final.member.dto.SignInReqDto;
import com.example.fever_final.member.dto.SignResultRepDto;
import com.example.fever_final.member.dto.UserJoinDto;
import com.example.fever_final.member.entity.Member;
import com.example.fever_final.member.repository.OuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignService {

    private final CustomMemeberDetailService customUserDetailService;
    private final OuthRepository outhRepository;
    private final Status status;
    private final ResponseMessage message;
    private final CommonEncoder passwordEncoder = new CommonEncoder();
    private final JwtTokenProvider jwtTokenProvider;


    /* U1 : 이름 중복여부 확인(실명) */
    public ResponseEntity validname(String nickname){
        Optional<Member> isExist = outhRepository.findByName(nickname);

        if(isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.EXISTED_NICKNAME
                    ,   new ResponseMessage().EXISTED_NICKNAME
            ), HttpStatus.NOT_FOUND);


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, nickname +" 닉네임  " + new ResponseMessage().SUCCESS, nickname
        ), HttpStatus.OK);
    }

    /* U2 :회원가입 API */
    public ResponseEntity signUp(UserJoinDto userJoinDto){
        Member user = new Member(userJoinDto);

        user.setRoles("USER"); // 역할(권한) 부여
        user.setPassword(passwordEncoder.encode(userJoinDto.getPassword())); // 패스워드 암호화

        int result = customUserDetailService.signUpUser(user);
        if (result == 1) {
            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, message.SUCCESS + " : 회원가입"), HttpStatus.OK);

        } else if (result == -1) {
            return new ResponseEntity(NoDataResponse.response(status.LOGIN_INVALID_ID, message.LOGIN_INVALID_ID), HttpStatus.OK);

        } else {

            return new ResponseEntity(NoDataResponse.response(status.LOGIN_INVALID_ID, message.LOGIN_INVALID_ID), HttpStatus.OK);
        }
    }

    /* U3 : 일반로그인 API */
    public ResponseEntity signIn(SignInReqDto signInReqDto){
        Member user = (Member) customUserDetailService.findByPhoneNumber(signInReqDto.getPhoneNumber());
//        Member user = (Member) customUserDetailService.findByPhoneNumber(signReqDto.getPhoneNumber());

        SignResultRepDto signResultRepDto = new SignResultRepDto();

        try {
            if (passwordEncoder.matches(signInReqDto.getPassword(), user.getPassword())) {
                System.out.println("비밀번호 일치");
//                List<String> roleList = Arrays.asList(user.getRoles().split(","));
                String roles = user.getRoles();

                signResultRepDto.setAccessToken(jwtTokenProvider.createToken(user.getPhoneNumber(), roles)); // access token 만들기
                String tmpRefreshToken = jwtTokenProvider.createRefreshToken(user.getPhoneNumber());
                signResultRepDto.setRefreshToken(tmpRefreshToken); // refresh token 만들기

                // 새롭게 DB에 refresh token 변경
                user.setRefreshToken(tmpRefreshToken);
                customUserDetailService.save(user);

                return new ResponseEntity(DataResponse.response(status.SUCCESS, message.SUCCESS + " : 로그인",signResultRepDto), HttpStatus.OK);

            } else {
                return new ResponseEntity(NoDataResponse.response(status.LOGIN_INVALID_PASSWORD, message.LOGIN_INVALID_PASSWORD), HttpStatus.OK);
            }
        } catch (NullPointerException e) {

            return new ResponseEntity(NoDataResponse.response(status.LOGIN_INVALID_ID, message.LOGIN_INVALID_ID), HttpStatus.OK);

        }
    }

    /* U4 : accessToken 재발급 API */
    public ResponseEntity getReaccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto){
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
