package com.example.fever_final.table.ticket.repository;


import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findAllByMemberAndIsChecked(Member member, int isChecked);
}
