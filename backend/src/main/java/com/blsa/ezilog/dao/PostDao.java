package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.post.Post;

public interface PostDao extends JpaRepository<Post, BigInteger>{
    
    @Query(value = "SELECT * FROM POST WHERE WRITER LIKE %:writer%",
            countQuery = "SELECT count(*) FROM POST WHERE WRITER LIKE %:writer%",
            nativeQuery = true)
    Page<Post> findPostByWriter(String writer, Pageable request);
    
    Optional<Post> findPostByNo(BigInteger no);
    
    
    
    @Query(value = "SELECT * FROM POST WHERE TITLE LIKE %:title%",
            countQuery = "SELECT count(*) FROM POST WHERE TITLE LIKE %:title%",
            nativeQuery = true)
    Page<Post> findPostByTitle(String title, Pageable request);
    
    @Query(value = "SELECT * FROM POST WHERE TITLE LIKE %:title% OR WRITER LIKE %:keyword%",
            countQuery = "SELECT count(*) FROM POST WHERE TITLE LIKE %:title% OR WRITER LIKE %:keyword%",
            nativeQuery = true)
    Page<Post> findPostByTitleORWriter(String keyword, Pageable request);
}
