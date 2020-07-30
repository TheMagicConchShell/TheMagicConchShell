package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.reply.Reply;

public interface ReplyDao extends JpaRepository<Reply, BigInteger> {
    
    Page<Reply> findReplyByPostNo(BigInteger postNo, Pageable request);
    
    @Query(value="SELECT * FROM reply WHERE post_no =:postNo", nativeQuery=true)
    List<Reply> ReplyByPostNum(BigInteger postNo);
    
    
    @Query(value = "SELECT * FROM reply WHERE writer LIKE %:writer% AND secret=false",
            countQuery = "SELECT count(*) FROM reply WHERE writer LIKE %:writer% AND secret=false",
            nativeQuery = true)
    Page<Reply> findReplyByWriter(String writer, Pageable request);
    
    //Reply findReplyById(BigInteger id);
    
    Optional<Reply> findReplyById(BigInteger id);
    
}
