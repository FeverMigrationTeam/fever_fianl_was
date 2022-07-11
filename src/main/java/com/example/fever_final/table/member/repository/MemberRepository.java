package com.example.fever_final.table.member.repository;

import com.example.fever_final.table.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByPhoneNumber(String phoneNumber);

    Optional<Member> findByName(String name);

}
