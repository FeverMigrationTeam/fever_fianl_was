package com.example.fever_final.table.payments.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UseTicketReqDto {

    private Long userId;
    private Long ticketId;
    private Long regalaId;

}
