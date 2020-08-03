package com.blsa.ezilog.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.point.PointHistory;
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

    @ApiOperation(value = "특정 유저가 가진 Point 이력, 총합 포인트 출력")
    @GetMapping
    public Object retrievePointUser(@RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        User useropt = userService.select(nickname);

        if (!useropt.equals(null)) {
            Map<String, Object> resultMap = new HashMap<>();

            Optional<List<PointHistory>> optList = pointService.selectPointByUser(BigInteger.valueOf(useropt.getUid()));
            if (optList.isPresent()) {
               
                int total = pointService.totalPoint(BigInteger.valueOf(useropt.getUid()));

                List<PointHistory> pList = optList.get();
                resultMap.put("pList", pList);
                resultMap.put("totalPoint", total);
                result.status = "S-200";
                result.message = "포인트 이력 가져오기에 성공했습니다.";
                result.data = resultMap;

                response = new ResponseEntity<>(result, HttpStatus.OK);

            }else {
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

}
