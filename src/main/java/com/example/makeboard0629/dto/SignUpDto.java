package com.example.makeboard0629.dto;

import com.example.makeboard0629.entity.Member;
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
    private List<String> roles;

    public Member toMemberEntity(){
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .roles(this.roles)
                .build();
    }
}
