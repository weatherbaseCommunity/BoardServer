package com.example.makeboard0629.repository;


import com.example.makeboard0629.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "select b from Board  b")
    List<Board> findAllBoard();

    @Query(value = "select b from  Board b where b.user.email =:email")
    List<Board> findByUserUniqueId(@Param("email") String email);

    List<Board> findAllByUserEmail(String email);
    Optional<Board> findById(Long id);

//    @Query(value = "select b from  Board b where b.id =:boardId")
//    Optional<Board> findById(@Param("boardId") Long boardId);



}