package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.like.ReplyLikeCount;

public interface ReplyLikeCountDao extends JpaRepository<ReplyLikeCount, BigInteger> {
    // 어떤 댓글에 좋아요, 싫어요 갯수가 몇개인지 타입에 따라 가져오기
    @Query(value = "SELECT count(*) FROM reply_like_count WHERE reply_id=:replyid AND type=:type", nativeQuery = true)
    Long countTotal(Long replyid, String type);
    
 // 현재 이 댓글에 내가 좋아요, 싫어요를 눌렀는지 체크
    @Query(value = "SELECT * FROM reply_like_count WHERE uid=:uid AND type=:type AND reply_id=:replyid", nativeQuery = true)
    Optional<ReplyLikeCount> checkExistLikeCount(Long uid, String type, Long replyid);
}
