package com.example.makeboard0629.service.board;
import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardUpdateDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public void saveBoard(BoardDto boardDto, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(NullPointerException::new);
        String[] hashTag = boardDto.getHashTag();
        StringBuilder sb = new StringBuilder();
        for (String tag : hashTag){
           sb.append(tag).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        Board board =Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .hashTag(sb.toString())
                .weatherUrl(boardDto.getWeatherUrl())
                .user(user)
                .build();
        boardRepository.save(board);
    }

    public void updateBoard(BoardUpdateDto boardUpdateDto, String userEmail){
        Optional<Board> boardOptional = boardRepository.findById(boardUpdateDto.getId());
        Board board;
        if (boardOptional.isPresent()){
            board = boardOptional.get();
            if (!board.getUser().getEmail().equals(userEmail)){
                throw new NullPointerException("updateBoard : 게시글 작성자가 아닙니다..");
            }
        }else throw new NullPointerException("updateBoard : 게시글을 찾을수 없습니다.");
        board.update(boardUpdateDto);
        boardRepository.save(board);
    }


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
            myBoards.add(new BoardsDto(board));
        }
        return myBoards;
    }





}
