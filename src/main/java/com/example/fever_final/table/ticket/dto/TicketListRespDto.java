package com.example.fever_final.table.ticket.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketListRespDto {

    private Long ticketId;
    private String ticketName;
    private int ticketType;

}
