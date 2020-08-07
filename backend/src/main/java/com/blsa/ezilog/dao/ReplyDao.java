package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.reply.Reply;

public interface ReplyDao extends JpaRepository<Reply, Long> {

    Page<Reply> findReplyByPostNo(Long postNo, Pageable request);

    @Query(value = "SELECT * FROM reply WHERE post_no =:postNo", nativeQuery = true)
    List<Reply> ReplyByPostNum(Long postNo);

    @Query(value = "SELECT * FROM reply WHERE writer LIKE %:writer% AND secret=false", countQuery = "SELECT count(*) FROM reply WHERE writer LIKE %:writer% AND secret=false", nativeQuery = true)
    Page<Reply> findReplyByWriter(String writer, Pageable request);

    @Query(value = "SELECT * FROM reply WHERE writer=:writer", 
            countQuery = "SELECT * FROM reply WHERE writer=:writer", nativeQuery = true)
    Page<Reply> findAllReplyByWriter(String writer, Pageable request);
    
    // Reply findReplyById(BigInteger id);

    Optional<Reply> findReplyById(Long id);

    @Query(value = "SELECT count(*) FROM reply WHERE post_no =:postNo", nativeQuery = true)
    int countByPostNum(Long postNo);
}
