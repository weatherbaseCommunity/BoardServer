package com.example.makeboard0629.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @JsonIgnore
    private String password;



    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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