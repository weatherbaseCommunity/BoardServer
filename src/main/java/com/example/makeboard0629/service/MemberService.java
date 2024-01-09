package com.example.makeboard0629.service;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.Comment;
import com.example.makeboard0629.entity.Like;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.repository.UserRepository;
import com.example.makeboard0629.type.Authority;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("couldn't find user" + email));

    }

    public User register(SignUpDto signUpDto){
        boolean exists = this.userRepository.existsByEmail(signUpDto.getEmail());
        if (exists){
            throw new RuntimeException("이미 사용중인 아이디 입니다");
        }
        User user = User.builder()
                .userRole(Authority.ROLE_USER)
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickName(UUID.randomUUID().toString().substring(0, 8))
                .boards(new ArrayList<Board>())
                .comments(new ArrayList<Comment>())
                .likes(new ArrayList<Like>())
                .build();
        return userRepository.saveAndFlush(user);
    }
    public User authenticate(SignInDto signInDto){
        var member = this.userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 ID 입니다"));
        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public User oauth2Register(SignUpDto signUpDto) {
        // 이미 가입된 아이디인지 확인
        boolean exists = userRepository.existsByEmail(signUpDto.getEmail());
        if (exists){
            System.out.println("이미 가입된 아이디 입니다. 자동 로그인을 진행합니다.");
            // 이미 가입된 경우 아이디를 이메일로 검색
            Optional<User> optionalMember = userRepository.findByEmail(signUpDto.getEmail());
            // 못찾을 경우 NullPointerException 출력
            User user = optionalMember.orElseThrow(()-> new NullPointerException("해당 이메일에 맞는 유저를 찾을수 없습니다."));
            // 비밀 번호 대조
            if (!passwordEncoder.matches(signUpDto.getPassword(), user.getPassword())){
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

            return user;


        }else {
            // 새로 가입하는 유저일경우
            System.out.println("처음가입하는 아이디 입니다. 자동로그인을 진행합니다.");
            // 유저 db에 저장
           return register(signUpDto);

//            Optional<User> optionalMember = userRepository.findByEmail(signUpDto.getEmail());
//            // 못찾을 경우 NullPointerException 출력
//
//            return optionalMember.orElseThrow(()-> new NullPointerException("해당 이메일에 맞는 유저를 찾을수 없습니다."));

        }

    }
    public User updateNickname(Long id, String nickname){
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();
        user.updateNickname(nickname);
        return userRepository.save(user);
    }
}
