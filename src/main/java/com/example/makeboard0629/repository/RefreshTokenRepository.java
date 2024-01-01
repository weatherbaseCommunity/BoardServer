package com.example.makeboard0629.repository;

import com.example.makeboard0629.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByKeyEmail(String email);
    boolean existsByKeyEmail(String userEmail);
    void deleteByKeyEmail(String userEmail);
}