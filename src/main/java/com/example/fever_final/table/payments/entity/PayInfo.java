package com.example.fever_final.table.payments.entity;


import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.payments.etc.PayType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pay_info")
@Builder
public class PayInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PayType payType = PayType.NICE;

    private String clientId;
    private String secretId;

    public static PayInfo buildPayInfo(Long userId, String clientId, String secretId) {
        return PayInfo.builder()
                .userId(userId)
                .clientId(clientId)
                .secretId(secretId)
                .build();
    }

}
