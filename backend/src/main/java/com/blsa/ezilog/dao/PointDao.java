package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.point.PointHistory;

public interface PointDao extends JpaRepository<PointHistory, Long>{
    
    //특정 유저가 가진 포인트 이력
    @Query(value="SELECT * FROM point_history WHERE uid=:uid",countQuery = "SELECT count(*) FROM point_history WHERE uid=:uid" ,nativeQuery = true)
    Page<PointHistory> getPointByUid(Long uid, Pageable request);
    //특정 유저의 전체 포인트 합계
    @Query(value="SELECT sum(change_val) FROM point_history WHERE uid=:uid", nativeQuery = true)
    Optional<Integer> totalPointByUid(Long uid);
    
    //포인트로 유저 랭킹 매기기
    @Query(value="SELECT * FROM point_history  GROUP BY uid ORDER BY sum(`change_val`)DESC LIMIT 5", nativeQuery = true)
    List<PointHistory> getRankByPoint();
    
    
}
