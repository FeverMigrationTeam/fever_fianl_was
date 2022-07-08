package com.example.fever_final.member.service;

import com.example.fever_final.member.entity.Member;
import com.example.fever_final.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomMemeberDetailService implements UserDetailsService {

    private final MemberRepository outhRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return outhRepository.findByPhoneNumber(phoneNumber);
    }

    public UserDetails findByPhoneNumber(String phoneNumber) {
        return outhRepository.findByPhoneNumber(phoneNumber);
    }

    public UserDetails findByEmail(String phoneNumber) {
        return outhRepository.findByPhoneNumber(phoneNumber);
    }

    public int signUpUser(Member member) {
        if (outhRepository.findByPhoneNumber(member.getPhoneNumber()) == null) {
            outhRepository.save(member);
            return 1; // 회원가입 성공시
        } else
            return -1; // 회원가입 실패시
    }

    public void deletUser(String email) { // 유저 삭제 ** 조금 위험함 사용할 때 다시 코드 수정하기
//        outhRepository.deleteByEmail(email);

    }

    public Optional<Member> findById(Long userId) {
        return outhRepository.findById(userId);
    }

    public void save(Member user) {
        if (user != null)
            outhRepository.save(user);
    }
}
