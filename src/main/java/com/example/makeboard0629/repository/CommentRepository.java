package com.example.makeboard0629.repository;

import com.example.makeboard0629.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);
    List<Comment> findAllByUserEmail(String uniqueId);
    void deleteById(Long commentId);

    Optional<Comment> findById(Long commentId);
}
