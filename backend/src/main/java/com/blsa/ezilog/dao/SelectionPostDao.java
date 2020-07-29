package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.post.SelectionPost;

public interface SelectionPostDao extends JpaRepository<SelectionPost, BigInteger>{

    Optional<SelectionPost> findByNo(BigInteger no);
    
    Page<SelectionPost> findByIdLessThanOrderByIdDesc(BigInteger id, Pageable pageRequest);

    void deleteByNo(BigInteger no);
    
}
