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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.PostDao;
import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.post.Post;
import com.blsa.ezilog.model.spot.SpotArea;
import com.blsa.ezilog.model.spot.SpotBanner;
import com.blsa.ezilog.model.spot.SpotCreateRequest;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.service.SpotService;
import com.blsa.ezilog.service.point.PointService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/spot")
public class SpotController {

    @Autowired
    SpotService spotService;

    @Autowired
    UserDao userDao;

    @Autowired
    PointService pointService;

    @Autowired
    PostDao postDao;

    @ApiOperation(value = "등록 된 Spot들 가져 오기", notes = "관리자 모드에서 보기 위한")
    @GetMapping
    public Object retrieveSpot(@RequestParam int page, @RequestHeader(value = "nickname") String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4600";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id");

            if ("admin".equals(nickname)) {
                Page<SpotArea> sList = spotService.ValidSpot(pageable);

                if (!sList.isEmpty()) {
                    result.status = "S-200";
                    result.message = "Spot 목록 불러오기에 성공했습니다.";
                    result.data = sList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4402";
                    eresult.message = "불러 올 Spot이  없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noSpot");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4601";
                eresult.message = "Spot Area 열람 권한이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "noAuthority");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        }

        return response;

    }

    @ApiOperation(value = "spot 가져오기 (배너용)")
    @GetMapping("/banner")
    public Object retrieveSpotBanner() {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();
        
        List<SpotArea> sList = spotService.AllSpot();
        List<SpotBanner> bList = new ArrayList<>();
        List<Post> pList = new ArrayList<>();
        if (!sList.isEmpty()) {
            sList.forEach((e) -> {
                pList.add(postDao.findById(e.getPostNo()).get());
//               Post temp = postDao.findById(e.getPostNo()).get();
//               bList.add(new SpotBanner(temp.getNo(), temp.getTitle()));
            });
            result.status = "S-200";
            result.message = "Spot table에 있는 post 목록 불러오기에 성공했습니다.";
            result.data = pList;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            eresult.status = "E-4402";
            eresult.message = "불러 올 Spot Post가  없습니다.";
            eresult.data = null;
            errorMap.put("field", "noSpot");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation(value = "Spot에 고민 등록")
    @PostMapping
    public Object registSpot(@RequestBody SpotCreateRequest request,
            @RequestHeader(value = "nickname") String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);
        Optional<Post> optPost = postDao.findById(request.getPostNo());

        // 회원인지 아닌지 테스트
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (optPost.isPresent()) {
                Post post = optPost.get();
                // post 글 작성자와 현재 spot add를 신청하는 작성자가 같을 경우
                if (post.getWriter().equals(nickname)) {
                    String iresult = spotService.InsertSpot(request, user);
                    if (iresult.equals("success")) {
                        user.setPoint(user.getPoint() - 500);
                        userDao.save(user);
                        result.status = "S-200";
                        result.message = "Spot 등록에 성공했습니다.";
                        result.data = "success";

                        response = new ResponseEntity<>(result, HttpStatus.OK);
                    } else if (iresult.equals("overUserRegist")) {
                        eresult.status = "E-4604";
                        eresult.message = "이미 Spot Area에 유효 이상의 Spot을 등록했습니다.";
                        eresult.data = null;
                        errorMap.put("field", "overUserSpot");
                        errorMap.put("data", nickname);
                        eresult.errors = errorMap;
                        response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);
                    } else if (iresult.equals("noPoint")) {
                        eresult.status = "E-4605";
                        eresult.message = "Spot을 등록하기 위한 Point가 부족합니다.";
                        eresult.data = null;
                        errorMap.put("field", "noPoint");
                        errorMap.put("data", nickname);
                        eresult.errors = errorMap;
                        response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);
                    } else if (iresult.equals("overPostRegist")) {
                        eresult.status = "E-4606";
                        eresult.message = "이미 Spot Area에 등록된 post입니다.";
                        eresult.data = null;
                        errorMap.put("field", "overPostSpot");
                        errorMap.put("data", nickname);
                        eresult.errors = errorMap;
                        response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    eresult.status = "E-4607";
                    eresult.message = "이미 Spot Area에 등록할 권한이 없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noAuthority");
                    errorMap.put("data", nickname);
                    eresult.errors = errorMap;
                    response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
                }
            } else {
                eresult.status = "E-4608";
                eresult.message = "잘못 된 Post no입니다.";
                eresult.data = null;
                errorMap.put("field", "notValidPostNo");
                errorMap.put("data", request.getPostNo());
                eresult.errors = errorMap;
                response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);
            }
        } else {
            eresult.status = "E-4603";
            eresult.message = "알 수 없는 유저 입니다";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation(value = "Spot에 고민 삭제")
    @DeleteMapping
    public Object deleteSpot(@RequestParam Long id, @RequestHeader(value = "nickname") String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if ("admin".equals(nickname)) {
            String dresult = spotService.deleteSpot(id);
            if (dresult.equals("success")) {
                result.status = "S-200";
                result.message = "Spot 삭제에 성공했습니다.";
                result.data = "success";

                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else if (dresult.equals("noSpot")) {
                eresult.status = "E-4402";
                eresult.message = "해당되는 Spot이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noSpot");
                errorMap.put("data", id);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        } else {
            eresult.status = "E-4601";
            eresult.message = "Spot Area 삭제 권한이 없습니다.";
            eresult.data = null;
            errorMap.put("field", "noAuthority");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
        }

        return response;

    }

}
