package com.example.makeboard0629.service;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.entity.Member;
import com.example.makeboard0629.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("couldn't find user" + email));

    }

    public Member register(SignUpDto signUpDto){
        boolean exists = this.memberRepository.existsByEmail(signUpDto.getEmail());
        if (exists){
            throw new RuntimeException("이미 사용중인 아이디 입니다");
        }
        signUpDto.setPassword(this.passwordEncoder.encode(signUpDto.getPassword()));
        return this.memberRepository.save(signUpDto.toMemberEntity());
    }
    public Member authenticate(SignInDto signInDto){
        var member = this.memberRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 ID 입니다"));
        if (!this.passwordEncoder.matches(signInDto.getPassword(), member.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}
