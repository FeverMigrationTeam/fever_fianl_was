package com.example.fever_final.table.videoshare;


import com.example.fever_final.table.member.entity.Member;
import com.example.fever_final.table.video.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "video_share")
public class VideoShare {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Video video;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Member> memberList = new ArrayList<>();


}
