package com.example.fever_final.table.alarm.repository;


import com.example.fever_final.table.alarm.entity.Alarm;
import com.example.fever_final.table.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.awt.desktop.OpenFilesEvent;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAllByMember(Member member);


}
