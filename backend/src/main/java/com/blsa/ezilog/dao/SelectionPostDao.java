package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.post.Post;
import com.blsa.ezilog.model.post.SelectionPost;

public interface SelectionPostDao extends JpaRepository<SelectionPost, Long>{

    Optional<SelectionPost> findByNo(Long no);
    

    void deleteByNo(Long no);
    
}
