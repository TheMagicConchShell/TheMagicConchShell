package com.blsa.ezilog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.SpotDao;
import com.blsa.ezilog.model.point.PointHistory;
import com.blsa.ezilog.model.spot.SpotArea;
import com.blsa.ezilog.model.spot.SpotCreateRequest;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.service.point.PointService;

@Service
public class SpotService {

    @Autowired
    SpotDao spotDao;

    @Autowired
    PointService pointService;

    public List<SpotArea> BannerSpot(){
        
        return spotDao.BannerSpot();
    }
    
    public Page<SpotArea> ValidSpot(Pageable request) {

        // Page<SpotArea> result = null;
        Page<SpotArea> sList = spotDao.allValidSpot(request);

        return sList;
    }

    public String InsertSpot(SpotCreateRequest request, User user) {
        String result = "";
        // 추가 할 Spot 정보

        // 현재 시간 출력
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime registDate = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
                currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute(), 0);

        SpotArea sa = new SpotArea(request.getPostNo(), registDate.plusMinutes(1), registDate.plusMinutes(11),
                user.getNickname());
        // 이미 유저 이름으로 등록된 Spot이 있는지 체크
        Optional<List<SpotArea>> optUserList = spotDao.checkValidUserSpot(user.getNickname());
        // 이미 post_no로 등록된 Spot이 있는지 체크
        Optional<SpotArea> optPost = spotDao.checkValidPostSpot(request.getPostNo());
        PointHistory point = new PointHistory(user.getUid(), -500, "고민 광고 등록");

        if (!optPost.isPresent()) {
            
            if (optUserList.isPresent()) {
                List<SpotArea> sList = optUserList.get();
                if (sList.size() < 2) { // 나중에 N개로 확장 할 때 필요한 조건문
                    if (pointService.addPoint(point)) {
                        spotDao.save(sa);
                        result = "success";
                    } else {
                        result = "noPoint";
                    }
                } else {
                    result = "overUserRegist";
                }
            } else { // owner로 된 spot이 없다면
                if (pointService.addPoint(point)) {
                    spotDao.save(sa);
                    result = "success";
                } else {
                    result = "noPoint";
                }
            }
        } else {
            result = "overPostRegist";
        }

        return result;
    }

    // 매일 12시에 실행 되도록 설정
    @Scheduled(cron = "0 * * * * ?")
    public void autoDelete() {
        // 주기적으로 spot_area table에서 만료가 된 내역들을 삭제.
        Optional<List<SpotArea>> optList = spotDao.allNotValidSpot();

        if (optList.isPresent()) {

            List<SpotArea> sList = optList.get();
            for (int i = 0; i < sList.size(); i++) {
                spotDao.delete(sList.get(i));
            }
        }
    }

    public String deleteSpot(Long id) {
        String result = "";

        if (spotDao.findById(id).isPresent()) {
            spotDao.deleteById(id);
            result = "success";
        } else {
            result = "noSpot";
        }
        return result;
    }
}
