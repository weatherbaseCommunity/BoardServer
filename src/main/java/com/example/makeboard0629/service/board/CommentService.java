package com.example.makeboard0629.service.board;

import com.example.makeboard0629.dto.board.CommentDto;
import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.Comment;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.repository.BoardRepository;
import com.example.makeboard0629.repository.CommentRepository;
import com.example.makeboard0629.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Comment writeComment(User user, CommentDto commentDto) {
        Board board = boardRepository.findById(commentDto.getBoardId()).get();
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .userNickname(user.getNickName())
                .board(board)
                .build();
        return commentRepository.save(comment);
    }

    public List<Comment> findAll(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public Comment editComment(User user, CommentDto commentDto) {
        Optional<Comment> optComment = commentRepository.findById(commentDto.getCommentId());
        Long a = user.getId();
        Long b = optComment.get().getUser().getId();

        if (!Objects.equals(a, b)) {
            return null;
        }

        Comment comment = optComment.get();
        comment.update(commentDto.getContent(), user.getNickName());

        return commentRepository.save(comment);


    }

    public boolean deleteComment(User user, Long commentId) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (!Objects.equals(user.getId(), optComment.get().getUser().getId())) {
            return false;
        }
        commentRepository.delete(optComment.get());
        return true;
    }
}
