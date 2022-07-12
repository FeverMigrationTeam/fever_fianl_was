package com.example.fever_final.table.video.entity;


import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.regala.entity.Regala;
import com.example.fever_final.table.stadium.entity.Stadium;
import com.example.fever_final.table.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Video extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regala_id")
    private Regala regala;



    public static Video buildUpload( String url,Member member, Stadium stadium,Regala regala){
        return Video.builder()
                .member(member)
                .stadium(stadium)
                .videoUrl(url)
                .regala(regala)
                .build();
    }

}
