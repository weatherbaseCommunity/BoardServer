package com.example.makeboard0629.service.board;

import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.Like;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.repository.BoardRepository;
import com.example.makeboard0629.repository.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    @Transactional
    public void addLike(Board board, User user) {

        if (checkLike(user.getId(), board.getId())){
            log.info("이미 좋아요를 눌렀습니다.");
            return;
        }

//        User boardUser = board.getUser();

        // 자신이 누른 좋아요가 아니라면
//        if (!boardUser.equals(user)) {
//            boardUser.likeChange(boardUser.getReceivedLikeCnt() + 1);
//        }
        board.changeLikeCnt(board.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
                .user(user)
                .board(board)
                .build());
    }

    @Transactional
    public void decreaseLike(Board board, User user) {
        if (!checkLike(user.getId(), board.getId())){
            log.info("해당 좋아요를 찾을수 없습니다.");
            return;
        }
//        User boardUser = board.getUser();

        // 자신이 누른 좋아요가 아니라면
//        if (!boardUser.equals(user)) {
//            boardUser.likeChange(boardUser.getReceivedLikeCnt() - 1);
//        }
        board.changeLikeCnt(board.getLikeCnt() - 1);

        likeRepository.deleteByUserIdAndBoardId(user.getId(), board.getId());
    }

    public Boolean checkLike(Long userId, Long boardId) {
        return likeRepository.existsByUserIdAndBoardId(userId, boardId);
    }
}
