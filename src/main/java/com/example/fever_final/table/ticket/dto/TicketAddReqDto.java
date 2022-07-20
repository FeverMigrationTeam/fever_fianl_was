package com.example.fever_final.table.ticket.dto;


import com.example.fever_final.table.ticket.etc.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketAddReqDto {

    private Long userId;
    private int ticketType; // 0 : short,  1 : Medium , 2 : Long


}
