package com.example.fever_final.table.video.entity;


import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.stadium.Stadium;
import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.video.dto.UploadReqDto;
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


    public static Video buildUpload( String url,Member member, Stadium stadium){
        return Video.builder()
                .member(member)
                .stadium(stadium)
                .videoUrl(url)
                .build();
    }

}
