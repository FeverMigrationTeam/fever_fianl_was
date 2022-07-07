package com.example.fever_final.member.repository;

import com.example.fever_final.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuthRepository extends JpaRepository<Member,Long> {

    Member findByPhoneNumber(String phoneNumber);
}
