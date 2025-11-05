package com.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @Column(name="num")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long num;
    @Column(unique = true)
    private String memberId;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setMemberId(memberFormDto.getMemberId());
        member.setPassword(memberFormDto.getPassword());
        member.setName(memberFormDto.getName());
        member.setPhone(memberFormDto.getPhone());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
}
