package com.example.fever_final.table.regala.entity;


import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.regala.etc.RegalaStatus;
import com.example.fever_final.table.stadium.entity.Stadium;
import com.example.fever_final.table.video.entity.Video;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "regala")
public class Regala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @Enumerated(EnumType.STRING)
    private RegalaStatus regalaStatus;

    public static Regala buildRegala(Stadium stadium,RegalaStatus regalaStatus){
        return Regala.builder()
                .stadium(stadium)
                .regalaStatus(regalaStatus)
                .build();

    }
}
