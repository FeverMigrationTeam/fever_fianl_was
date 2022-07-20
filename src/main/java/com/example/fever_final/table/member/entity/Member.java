package com.example.fever_final.table.member.entity;


import com.example.fever_final.table.member.dto.request.UserJoinDto;
import com.example.fever_final.table.member.etc.MemberStatus;
import com.example.fever_final.common.type.SocialType;
import com.example.fever_final.common.Timestamped;
import com.example.fever_final.table.payments.entity.PayInfo;
import com.example.fever_final.table.ticket.entity.Ticket;
import com.example.fever_final.table.video.entity.Video;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member extends Timestamped implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memeber_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private final MemberStatus status = MemberStatus.NORMAL;

    private String name; // 실명
    private String phoneNumber; // 핸드폰 번호 -> 휴대폰인증 구현해야됨.
    private String password;


    private String roles;


    @Enumerated(value = EnumType.STRING)
    private SocialType socialType = SocialType.NOT;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Ticket> ticketList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Video> videoList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private PayInfo payInfo;

//    @OneToMany(mappedBy = "member")
//    private List<Video> videoList = new ArrayList<Video>();

    private String refreshToken;


    public void setTicketList(Ticket ticket) {
        this.ticketList.add(ticket);

        if (ticket.getMember() != this)
            ticket.setMember(this);
    }

    public Member(UserJoinDto userJoinDto) {
        this.name = userJoinDto.getName();
        this.phoneNumber = userJoinDto.getPhoneNumber();
        this.password = userJoinDto.getPassword();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(roles));
        return auth;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
