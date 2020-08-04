package com.blsa.ezilog.service.point;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.blsa.ezilog.model.point.PointHistory;

public interface PointService {
     
    Optional<List<PointHistory>> selectPointByUser(BigInteger Uid);
    
    boolean addPoint(PointHistory point);
    
    int totalPoint(BigInteger Uid);
    
    void deletePoint(BigInteger id);
}
