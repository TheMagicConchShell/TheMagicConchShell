package com.blsa.ezilog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.point.PointHistory;
import com.blsa.ezilog.model.point.PointRank;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.service.UserService;
import com.blsa.ezilog.service.point.PointService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/point")

public class PointController {

    @Autowired
    UserService userService;

    @Autowired
    PointService pointService;

    @Autowired
    UserDao userDao;

    @ApiOperation(value = "특정 유저가 가진 Point 이력, 총합 포인트 출력")
    @GetMapping
    public Object retrievePointUser(@RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        User useropt = userService.select(nickname);

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id");

        if (useropt != null) {
            Map<String, Object> resultMap = new HashMap<>();

            Page<PointHistory> phList = pointService.selectPointByUser(useropt.getUid(), pageable);
            if (!phList.isEmpty()) {

                int total = pointService.totalPoint(useropt.getUid());

                resultMap.put("pList", phList);
                resultMap.put("totalPoint", total);
                result.status = "S-200";
                result.message = "포인트 이력 가져오기에 성공했습니다.";
                result.data = resultMap;

                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "E-4500";
                eresult.message = "포인트 이력이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPointHistory");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        } else {
            eresult.status = "E-4501";
            eresult.message = "알 수 없는 회원 입니다. 포인트 이력을 가져 올 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "특정 유저가 가진 총합 포인트 출력")
    @GetMapping("/rank/total-point")
    public Object retrieveRankByPoint(@RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        User useropt = userService.select(nickname);

        if (!useropt.equals(null)) {

            int total = pointService.totalPoint(useropt.getUid());

            result.status = "S-200";
            result.message = "포인트 이력 가져오기에 성공했습니다.";
            result.data = total;

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            eresult.status = "E-4501";
            eresult.message = "알 수 없는 회원 입니다. 포인트 이력을 가져 올 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    @ApiOperation(value = "특정 유저가 가진 Point 이력, 총합 포인트 출력")
    @GetMapping("/total")
    public Object retrievePointHistory(@RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        List<PointHistory> optList = pointService.selectRankByTotalPoint();

        if (optUser.isPresent()) {
            if (!optList.isEmpty()) {
                List<PointHistory> rList = optList;

                List<PointRank> prList = new ArrayList<>();
                int total = 0;
                for (int i = 0; i < rList.size(); i++) {
                    total = pointService.totalPoint(rList.get(i).getUid());
                    User utemp = userDao.getOne(rList.get(i).getUid().longValue());
                    prList.add(new PointRank(utemp.getNickname(), total));
                }

                result.status = "S-200";
                result.message = "포인트 랭킹 가져오기에 성공했습니다.";
                result.data = prList;

                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "E-4500";
                eresult.message = "포인트 이력이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPointHistory");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        } else {
            eresult.status = "E-4504";
            eresult.message = "알 수 없는 회원 입니다. 내역을 가져 올 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "유저 레벨업 ")
    @PutMapping("/level-up")
    public Object userLevelUp(@RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);

        Map<Integer, Integer> requireEP = new HashMap<>();

        // level 업에 필요 한 경험치 (1랩은 :100, 2랩은 200... 등차 수열)
        for (int i = 0; i < 30; i++) {
            requireEP.put(i + 1, (i + 1) * 100);
        }

        // 회원인 유저가 접근 했는지 확인

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 유저가 만랩 인 경우
            if (user.getLevel() < 30) {
                // (현재 유저가 가진 포인트가 래밸업에 필요한 포인트보다 많거나 같다면)
                if (user.getPoint() >= requireEP.get(user.getLevel() + 1)) {
                	int nextLv = user.getLevel() + 1;
                    // 레밸업에 필요한 포인트 만큼 감소
                    user.setPoint(user.getPoint() - requireEP.get(nextLv));
                    // 유저 레벨 변화 적용
                    user.setLevel(nextLv);
                    userDao.save(user);
                    
                    PointHistory p = new PointHistory(user.getUid(), -requireEP.get(nextLv), "Lv."+nextLv+" 레벨업");
                    pointService.addPoint(p);

                    result.status = "S-200";
                    result.message = "래벨 업 성공했습니다.";
                    result.data = user.getLevel();

                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    eresult.status = "E-4502";
                    eresult.message = "레벨 업에 필요한 포인트가 부족합니다. 레벨업을 진행 할 수 없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noEnoughPoint");
                    errorMap.put("needPoint", requireEP.get(user.getLevel() + 1) - user.getPoint());
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
                }
            } else {
                eresult.status = "E-4503";
                eresult.message = "더이상 레벨업을 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "highest-level");
                errorMap.put("user-level", user.getLevel());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            }

        } else {
            eresult.status = "E-4504";
            eresult.message = "알 수 없는 회원 입니다. 레벨업을 진행 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;

    }

}
