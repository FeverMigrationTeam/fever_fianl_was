package com.example.fever_final.table.video.repository;

import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    
    List<Video> findAllByMember(Member member);
}
