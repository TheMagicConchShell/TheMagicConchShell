package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.post.Post;

public interface PostDao extends JpaRepository<Post, BigInteger>{
    
    @Query(value = "SELECT * FROM post WHERE writer LIKE %:writer%",
            countQuery = "SELECT count(*) FROM post WHERE writer LIKE %:writer%",
            nativeQuery = true)
    Page<Post> findPostByWriter(String writer, Pageable request);
    
    Optional<Post> findPostByNo(BigInteger no);
    
    
    
    @Query(value = "SELECT * FROM post WHERE title LIKE %:title%",
            countQuery = "SELECT count(*) FROM post WHERE title LIKE %:title%",
            nativeQuery = true)
    Page<Post> findPostByTitle(String title, Pageable request);
    
    @Query(value = "SELECT * FROM post WHERE title LIKE %:title% OR writer LIKE %:keyword%",
            countQuery = "SELECT count(*) FROM post WHERE title LIKE %:title% OR writer LIKE %:keyword%",
            nativeQuery = true)
    Page<Post> findPostByTitleORWriter(String keyword, Pageable request);
    
    @Query(value = "SELECT * FROM post WHERE allow = true AND no not in (SELECT NO FROM selection_history)",
            countQuery = "SELECT count(*) FROM post WHERE allow = true",
            nativeQuery = true)
    Page<Post> findPostByAllowIsTrueAndNotInHistory(Pageable request);
}
