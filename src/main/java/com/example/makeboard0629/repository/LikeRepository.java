package com.example.makeboard0629.repository;

import com.example.board.entity.Like;
import com.example.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByUserUniqueIdAndBoardId(String uniqueId, Long boardId);
    Boolean existsByUserNickNameAndBoardId(String userNickname, Long boardId);
    List<Like> findAllByUserUniqueId(String uniqueId);
    List<Like> findAllByBoardId(Long boardId);
    Optional<Like> findByUserAndBoardId(User user, Long board_id);

}
