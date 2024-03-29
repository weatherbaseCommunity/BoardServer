package com.example.makeboard0629.service.board;

import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardUpdateDto;
import com.example.makeboard0629.dto.board.BoardsDto;
import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.Comment;
import com.example.makeboard0629.entity.Like;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.repository.BoardRepository;
import com.example.makeboard0629.repository.CommentRepository;
import com.example.makeboard0629.repository.LikeRepository;
import com.example.makeboard0629.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CommentService commentService;

    public void saveBoard(BoardDto boardDto, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(NullPointerException::new);
        String[] hashTag = boardDto.getHashTag();
        StringBuilder sb = new StringBuilder();
        for (String tag : hashTag) {
            sb.append(tag).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .hashTag(sb.toString())
                .gradation(boardDto.getGradation())
                .season(boardDto.getSeason())
                .weather(boardDto.getWeather())
                .country(boardDto.getCountry())
                .timeZone(boardDto.getTimeZone())
                .user(user)
                .build();
        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(BoardUpdateDto boardUpdateDto, String userEmail) {
        Optional<Board> boardOptional = boardRepository.findById(boardUpdateDto.getId());
        Board board;
        if (boardOptional.isPresent()) {
            board = boardOptional.get();
            if (!board.getUser().getEmail().equals(userEmail)) {
                throw new NullPointerException("updateBoard : 게시글 작성자가 아닙니다..");
            }
        } else throw new NullPointerException("updateBoard : 게시글을 찾을수 없습니다.");
        board.update(boardUpdateDto);
        boardRepository.save(board);
    }


    public List<BoardsDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllBoard();
        List<BoardsDto> boardsDtoList = new ArrayList<>();
        for (Board board : boards) {
            Long boardId = board.getId();
            List<Comment> commentList = commentService.findAll(boardId);
            boardsDtoList.add(new BoardsDto(board, commentList));
        }
        return boardsDtoList;
    }

    public List<BoardsDto> getMyBoards(String uniqueId) {
        List<BoardsDto> myBoards = new ArrayList<>();
        List<Board> myBoard = boardRepository.findByUserEmail(uniqueId);
        for (Board board : myBoard) {
            Long boardId = board.getId();
            List<Comment> commentList = commentService.findAll(boardId);
            myBoards.add(new BoardsDto(board, commentList));
        }
        return myBoards;
    }

    public Board findBoardById(Long boardId) {
        Optional<Board> byId = boardRepository.findById(boardId);
        return byId.orElse(null);
    }

    public List<BoardsDto> search(String type, String search) {
        List<BoardsDto> boardsDtoList = new ArrayList<>();
        if (type.equals("hashTag")) {
            List<Board> byHashTagContaining = boardRepository.findByHashTagContaining(search);
            for (Board board : byHashTagContaining) {
                Long boardId = board.getId();
                List<Comment> commentList = commentService.findAll(boardId);
                boardsDtoList.add(new BoardsDto(board, commentList));
            }
            return boardsDtoList;
        } else if (type.equals("title")) {
            List<Board> byTitleContaining = boardRepository.findByTitleContaining(search);
            for (Board board : byTitleContaining) {
                Long boardId = board.getId();
                List<Comment> commentList = commentService.findAll(boardId);
                boardsDtoList.add(new BoardsDto(board, commentList));
            }
            return boardsDtoList;
        } else if (type.equals("content")) {
            List<Board> byContentContaining = boardRepository.findByContentContaining(search);
            for (Board board : byContentContaining) {
                Long boardId = board.getId();
                List<Comment> commentList = commentService.findAll(boardId);
                boardsDtoList.add(new BoardsDto(board, commentList));
            }
            return boardsDtoList;
        } else {
            throw new NullPointerException("찾을수가 없습니다.");
        }
    }

    @Transactional
    public void deleteBoard(User loginUser, Long boardId) {
        User user = userRepository.findById(loginUser.getId()).get();
        Board board = boardRepository.findById(boardId).get();

        if (board.getUser().getId() != user.getId()) {
            throw new RuntimeException("사용자와 게시물 작성자가 맞지 않습니다.");
        } else {
            likeRepository.deleteAllByBoardId( boardId);
            commentRepository.deleteAllByBoardId(boardId);
            boardRepository.deleteById(boardId);
        }


    }

}
