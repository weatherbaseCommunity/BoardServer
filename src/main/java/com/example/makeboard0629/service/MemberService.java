package com.example.makeboard0629.service;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.entity.Users;
import com.example.makeboard0629.repository.UsersRepository;
import com.example.makeboard0629.type.Authority;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("couldn't find user" + email));

    }

    public Users register(SignUpDto signUpDto){
        boolean exists = this.usersRepository.existsByEmail(signUpDto.getEmail());
        if (exists){
            throw new RuntimeException("이미 사용중인 아이디 입니다");
        }
        Users user = Users.builder()
                .userRole(Authority.ROLE_USER)
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();
        return usersRepository.save(user);
    }
    public Users authenticate(SignInDto signInDto){
        var member = this.usersRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 ID 입니다"));
        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public Users oauth2Register(SignUpDto signUpDto) {
        // 이미 가입된 아이디인지 확인
        boolean exists = usersRepository.existsByEmail(signUpDto.getEmail());
        if (exists){
            System.out.println("이미 가입된 아이디 입니다. 자동 로그인을 진행합니다.");
            // 이미 가입된 경우 아이디를 이메일로 검색
            Optional<Users> optionalMember = usersRepository.findByEmail(signUpDto.getEmail());
            // 못찾을 경우 NullPointerException 출력
            Users user = optionalMember.orElseThrow(()-> new NullPointerException("해당 이메일에 맞는 유저를 찾을수 없습니다."));
            // 비밀 번호 대조
            if (!passwordEncoder.matches(signUpDto.getPassword(), user.getPassword())){
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

            return user;


        }else {
            // 새로 가입하는 유저일경우
            System.out.println("처음가입하는 아이디 입니다. 자동로그인을 진행합니다.");
            // 유저 db에 저장
            Users user = Users.builder()
                    .email(signUpDto.getEmail())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .userRole(Authority.ROLE_USER)
                    .build();
            usersRepository.saveAndFlush(user);

            Optional<Users> optionalMember = usersRepository.findByEmail(signUpDto.getEmail());
            // 못찾을 경우 NullPointerException 출력

            return optionalMember.orElseThrow(()-> new NullPointerException("해당 이메일에 맞는 유저를 찾을수 없습니다."));

        }

    }
}
