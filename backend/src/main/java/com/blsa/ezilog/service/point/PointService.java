package com.blsa.ezilog.service.point;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blsa.ezilog.model.point.PointHistory;

public interface PointService {
     
    public Page<PointHistory> selectPointByUser(BigInteger Uid, Pageable pageable);
    
    List<PointHistory> selectRankByTotalPoint();
    
    boolean addPoint(PointHistory point);
    
    int totalPoint(BigInteger Uid);
    
    void deletePoint(BigInteger id);
}
