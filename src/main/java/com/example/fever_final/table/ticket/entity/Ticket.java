package com.example.fever_final.table.ticket.entity;


import com.example.fever_final.table.ticket.etc.TicketType;
import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ticket")
public class Ticket extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_memeber_id")
    private Member member;

    @Column(name = "ticket_name")
    private String ticketName; // ex) 30분 이용권

    @Enumerated(value = EnumType.STRING)
    private TicketType ticketType = TicketType.SHORT;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int isChecked; // 1: 사용가능 , 0: 사용됨. -> 0일때는 사용내역으로

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getTicketList().remove(this);
        }
        this.member = member;
        if (!member.getTicketList().contains(this))
            member.setTicketList(this);
    }




}



