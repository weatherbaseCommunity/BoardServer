package com.example.makeboard0629.repository;

import com.example.makeboard0629.entity.Like;
import com.example.makeboard0629.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByUserIdAndBoardId(Long userId, Long boardId);
    void deleteAllByBoardId(Long boardId);
    void deleteAllByUserId(Long userId);
    Boolean existsByUserIdAndBoardId(Long userId, Long boardId);
    List<Like> findAllByUserId(Long userId);
    List<Like> findAllByBoardId(Long boardId);
    Optional<Like> findByUserAndBoardId(User user, Long board_id);


}
