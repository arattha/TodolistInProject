package com.web.tip.member;

import com.web.tip.member.security.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "is_use")
    private boolean isUse;

    @Enumerated(EnumType.STRING)
    private Authority authority;
}