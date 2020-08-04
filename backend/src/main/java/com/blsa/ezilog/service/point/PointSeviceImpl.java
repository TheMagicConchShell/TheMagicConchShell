package com.blsa.ezilog.service.point;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.PointDao;
import com.blsa.ezilog.model.point.PointHistory;

@Service
public class PointSeviceImpl implements PointService {

    @Autowired
    PointDao pointDao;

    @Override
    public Optional<List<PointHistory>> selectPointByUser(BigInteger Uid) {

        return pointDao.getPointByUid(Uid);
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
    public void deletePoint(BigInteger id) {
        PointHistory ptemp = pointDao.getOne(id);

        pointDao.delete(ptemp);

    }

    @Override
    public int totalPoint(BigInteger Uid) {
        int total = 0;
        Optional<Integer> optTotal = pointDao.totalPointByUid(Uid);
        if (optTotal.isPresent()) {
            total = optTotal.get();
        }
        return total;
    }

}
