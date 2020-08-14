package com.blsa.ezilog.service.point;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.PointDao;
import com.blsa.ezilog.model.point.PointHistory;

@Service
public class PointSeviceImpl implements PointService {

    @Autowired
    PointDao pointDao;

    @Override
    public Page<PointHistory> selectPointByUser(Long Uid, Pageable pageable) {

        return pointDao.getPointByUid(Uid, pageable);
    }

    @Override
    public boolean addPoint(PointHistory point) {
        boolean result = false;

        int total = totalPoint(point.getUid());
        if (!(total + point.getChange() < 0)) {
            pointDao.save(point);

            result = true;

        }

        return result;
    }

    @Override
    public void deletePoint(Long id) {
        PointHistory ptemp = pointDao.getOne(id);

        pointDao.delete(ptemp);

    }

    @Override
    public int totalPoint(Long Uid) {
        int total = 0;
        // 포인트 이력이 있으면 그것으로 계산, 없으면 0 점
        Optional<Integer> optTotal = pointDao.totalPointByUid(Uid);
        if (optTotal.isPresent()) {
            total = optTotal.get();
        }
        return total;
    }

    @Override
    public List<PointHistory> selectRankByTotalPoint() {

        return pointDao.getRankByPoint();
        // return null;
    }

}
