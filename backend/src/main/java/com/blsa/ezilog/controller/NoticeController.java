package com.blsa.ezilog.controller;

import java.math.BigInteger;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.NoticeDao;
import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.notice.Notice;
import com.blsa.ezilog.model.notice.NoticeCreateRequest;
import com.blsa.ezilog.model.notice.NoticeUpdateRequest;
import com.blsa.ezilog.model.user.User;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/support/notice")
public class NoticeController {

    @Autowired
    NoticeDao noticeDao;

    @Autowired
    UserDao userDao;

    @ApiOperation(value = "공지사항 목록 반환", notes = "Input : page, Output: 성공 : [status = true, data = 공지사항 리스트(Notice)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping
    public Object retrieveNotice(@RequestParam int page) {

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "nid");

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Page<Notice> sList = noticeDao.findAll(pageable);
        if (!sList.isEmpty()) {
            result.status = "S-200";
            result.message = "공지사항 목록 불러오기에 성공했습니다.";
            result.data = sList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4200";
            eresult.message = "불러 올 공지사항이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "noticeEmpty");
            errorMap.put("data", pageable);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @ApiOperation(value = "검색어에 해당 되는 작성자가 쓴 공지사항 반환", notes = "작성자 이름을 통해서 공지사항 검색", response = List.class)
    @GetMapping("/writer")
    public Object searchNoticeByWriter(@RequestParam String writer, @RequestParam int page) {

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "nid");

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Page<Notice> nList = noticeDao.findNoticeByWriter(writer, pageable);

        if (!nList.isEmpty()) {
            result.status = "S-200";
            result.message = "작성자를 이용하여 공지사항 목록들 가져오기 성공";
            result.data = nList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4201";
            eresult.message = "불러 올 공지사항이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "noticeEmpty");
            errorMap.put("data", pageable);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation(value = "공지 번호에 해당되는 공지사항 반환", response = List.class)
    @GetMapping("/noticeId")
    public Object seasrchNoticeById(@RequestParam(required = true) Long id) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Notice notice = noticeDao.getNoticeByNid(id);

        if (notice != null) {

            result.status = "S-200";
            result.message = "공지 ID를 이용하여 공지사항 가져오기 성공";
            result.data = notice;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "S-4202";
            eresult.message = "불러 올 공지사항이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "noNotice");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @ApiOperation(value = "공지사항 작성", notes = "Input: (제목, 내용, 작성자) OutPut: 성공(status = true, data= sucess), 실패(status=false, data=오류 디버그 메세지)", response = List.class)
    @PostMapping
    public Object insertNotice(@RequestBody NoticeCreateRequest notice,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            if (nickname.equals("admin")) {
                LocalDateTime currentTime = LocalDateTime.now();
                Notice temp = new Notice(notice.getTitle(), notice.getContent(), nickname, currentTime);
                noticeDao.save(temp);
                result.status = "S-200";
                result.message = "공지사항 작성에 성공했습니다.";
                result.data = null;
                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "E-4204";
                eresult.message = "관리자 계정이 아닙니다. 공지사항을 작성 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "creatNotice");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4207";
            eresult.message = "알수 없는 회원 입니다. 공지사항을 생성 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "createNotice");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "공지사항 삭제", response = List.class)
    @DeleteMapping
    public Object deleteNotice(@RequestParam Long nid, @RequestParam String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            if (nickname.equals("admin")) {
                Notice temp = noticeDao.getNoticeByNid(nid);
                noticeDao.delete(temp);

                result.status = "S-200";
                result.message = "공지사항 삭제 완료";
                result.data = null;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "E-4206";
                eresult.message = "관리자 계정이 아닙니다. 공지사항을 삭제 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deleteNotice");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4207";
            eresult.message = "알수 없는 회원 입니다. 공지사항을 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "createNotice");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "공지사항 내용 변경", response = List.class)
    @PutMapping
    public Object updateNotice(@RequestBody NoticeUpdateRequest notice,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);
        if (userOpt.isPresent()) {
            if (nickname.equals("admin")) {
                Notice updateTemp = noticeDao.getNoticeByNid(notice.getNid());
                updateTemp.setTitle(notice.getTitle());
                updateTemp.setContent(notice.getContent());
                LocalDateTime currentTime = LocalDateTime.now();
                updateTemp.setWriteDate(currentTime);

                noticeDao.save(updateTemp);
                result.status = "S-200";
                result.message = "공지사항 수정 완료";
                result.data = null;

                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "E-4208";
                eresult.message = "관리자 계정이 아닙니다. 공지사항을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updateNotice");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4207";
            eresult.message = "알수 없는 회원 입니다. 공지사항을 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "updateNotice");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;

    }
}
