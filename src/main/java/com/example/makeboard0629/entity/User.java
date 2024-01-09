package com.example.makeboard0629.entity;

import com.example.makeboard0629.type.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private Authority userRole;



    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();     // 작성글

    public void addBoard(Board board){
        boards.add(board);
        board.setUser(this);
    }

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();       // 유저가 누른 좋아요

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 댓글




    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(()->{return String.valueOf(this.getUserRole());});
        return authorityList;
    }

    @Override@JsonIgnore
    public String getUsername() {
        return null;
    }

    @Override@JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override@JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override@JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override@JsonIgnore
    public boolean isEnabled() {
        return false;
    }
}