package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.point.PointHistory;

public interface PointDao extends JpaRepository<PointHistory, BigInteger>{
    
    //특정 유저가 가진 포인트 이력
    @Query(value="SELECT * FROM point_history WHERE uid=:uid", nativeQuery = true)
    Optional<List<PointHistory>> getPointByUid(BigInteger uid);
    //특정 유저의 전체 포인트 합계
    @Query(value="SELECT sum(change_val) FROM point_history WHERE uid=:uid", nativeQuery = true)
    Optional<Integer> totalPointByUid(BigInteger uid);
}
