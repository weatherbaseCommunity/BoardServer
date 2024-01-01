package com.example.makeboard0629.dto;

import com.example.makeboard0629.entity.Member;
import com.example.makeboard0629.type.Authority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SignUpDto {
    private String email;
    private String password;
//    private String userRole;

    public Member toMemberEntity(){
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .userRole(Authority.ROLE_USER)
                .build();
    }
}
