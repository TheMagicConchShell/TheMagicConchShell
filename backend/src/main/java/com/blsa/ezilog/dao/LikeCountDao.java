package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.like.LikeCount;

public interface LikeCountDao extends JpaRepository<LikeCount, Long> {

    // 현재 이글에 내가 좋아요, 싫어요를 눌렀는지 체크
    @Query(value = "SELECT * FROM like_count WHERE uid=:uid AND type=:type AND post_no=:postno", nativeQuery = true)
    Optional<LikeCount> checkExistLikeCount(Long uid, String type, Long postno);
    
    // 현재 이글에 내가 좋아요, 싫어요를 눌렀는지 체크
    @Query(value = "SELECT * FROM like_count WHERE uid=:uid AND post_no=:postno", nativeQuery = true)
    Optional<LikeCount> checkExistLikeCountNoType(Long uid, Long postno);

    // 어떤 포스트에 좋아요, 싫어요 갯수가 몇개인지 타입에 따라 가져오기
    @Query(value = "SELECT count(*) FROM like_count WHERE post_no=:postno AND type=:type", nativeQuery = true)
    Long countTotal(Long postno, String type);

    @Query(value = "SELECT * FROM like_count WHERE post_no=:postno AND uid=:userid", nativeQuery = true)
    Optional<LikeCount> findLikeCountByUid(Long postno, Long userid);
}
