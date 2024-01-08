package com.example.makeboard0629.service.board;
import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardsDto;
import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.repository.BoardRepository;
import com.example.makeboard0629.repository.CommentRepository;
import com.example.makeboard0629.repository.LikeRepository;
import com.example.makeboard0629.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public void saveBoard(BoardDto boardDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NullPointerException::new);
        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .hashTag(boardDto.getHashTag())
                .weatherUrl(boardDto.getWeatherUrl())
                .user(user)
                .build();

        boardRepository.save(board);
    }

//    public void updateBoard(BoardsDto boardsDto, Long boardId){
//        Optional<Board> boardOptional = boardRepository.findById(boardId);
//        Board board;
//        if (boardOptional.isPresent()){
//            board = boardOptional.get();
//        }else throw new NullPointerException("updateBoard : 게시글을 찾을수 없습니다.");
//        board.updateBoard(boardsDto.getTitle(), boardsDto.);
//    }


    public List<BoardsDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllBoard();
        List<BoardsDto> boardsDtoList = new ArrayList<>();
        for (Board board : boards) {
            boardsDtoList.add(new BoardsDto(board));
        }
        return boardsDtoList;
    }

    public List<BoardsDto> getMyBoards(String uniqueId) {
        List<BoardsDto> myBoards = new ArrayList<>();
        List<Board> myBoard = boardRepository.findByUserEmail(uniqueId);
        for (Board board : myBoard) {
            myBoards.add(BoardsDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .commentCnt(board.getCommentCnt())
                    .lickCnt(board.getLikeCnt())
                    .weatherUrl(board.getWeatherUrl())
                    .hashTag(board.getHashTag())
                    .build());
        }
        return myBoards;
    }





}
