package com.blsa.ezilog.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.NoticeDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.notice.Notice;
import com.blsa.ezilog.model.notice.NoticeCreateRequest;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/support/notice")
public class NoticeController {

    @Autowired
    NoticeDao noticeDao;


    @ApiOperation(value = "공지사항 목록 반환", notes = "Input : no, Output: 성공 : [status = true, data = 공지사항 리스트(Notice)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping
    public Object retrieveNotice() {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            List<Notice> sList = noticeDao.findAll();
            result.status = "S-200";
            result.message = "공지사항 목록 불러오기에 성공했습니다.";
            result.data = sList;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            eresult.status = "E-4200";
            eresult.message = "서버 내부 오류로 인해 공지사항 목록 불러오기 실패.";
            eresult.data = null;
            errorMap.put("field", "getNotice");
            errorMap.put("data", e.getMessage());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;

    }

    @ApiOperation(value = "검색어에 해당 되는 작성자가 쓴 공지사항 반환", response = List.class)
    @GetMapping("/writer")
    public ResponseEntity<Map<String, Object>> searchNoticeByWriter(@RequestParam String writer) {

        ResponseEntity<Map<String, Object>> entity = null;

        try {
            List<Notice> sList = noticeDao.getNoticeByWriter(writer);
            entity = handleSuccess(sList);

        } catch (Exception e) {
            e.printStackTrace();
            entity = handleException(e);
        }
        return entity;
    }

    @ApiOperation(value = "공지 번호에 해당되는 공지사항 반환", response = List.class)
    @GetMapping("/noticeId")
    public Object seasrchNoticeById(@RequestParam BigInteger id) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Notice notice = noticeDao.getNoticeByNid(id);
            result.status = "S-200";
            result.message = "공지 ID를 이용하여 공지사항 가져오기 성공";
            result.data = notice;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            eresult.status = "E-4202";
            eresult.message = "서버 내부 오류로 인해 ID를 이용한 공지사항 가져오기 실패";
            eresult.data = null;

            errorMap.put("field", "getNoticeById");
            errorMap.put("data", null);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;

    }

    @ApiOperation(value = "공지사항 작성", notes = "Input: (제목, 내용, 작성자) OutPut: 성공(status = true, data= sucess), 실패(status=false, data=오류 디버그 메세지)", response = List.class)
    @PostMapping
    public Object insertNotice(@RequestBody NoticeCreateRequest notice) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Notice temp = new Notice(notice.getTitle(), notice.getContent(), notice.getWriter());
            noticeDao.save(temp);
            result.status = "S-200";
            result.message = "공지사항 작성에 성공했습니다.";
            result.data = null;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            eresult.status = "E-4203";
            eresult.message = "서버 내부 오류로 인해 공지사항 작성 실패.";
            eresult.data = null;
            errorMap.put("field", "creatNotice");
            errorMap.put("data", e.getMessage());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation(value = "공지사항 삭제", response = List.class)
    @DeleteMapping
    public Object deleteNotice(@RequestParam BigInteger nid) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Notice temp = noticeDao.getNoticeByNid(nid);
            noticeDao.delete(temp);
            
            result.status = "S-200";
            result.message = "공지사항 삭제 완료";
            result.data = null;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            eresult.status = "E-4204";
            eresult.message = "서버 내부 오류로 인해 공지사항 삭제 실패";
            eresult.data = null;

            errorMap.put("field", "deleteNotice");
            errorMap.put("data", nid);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation(value = "공지사항 내용 변경", response = List.class)
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateNotice(@RequestBody Notice notice) {
        ResponseEntity<Map<String, Object>> entity = null;

        try {
            Notice updateTemp = noticeDao.getNoticeByNid(notice.getNid());
            updateTemp.setTitle(notice.getTitle());
            updateTemp.setContent(notice.getContent());

            noticeDao.save(updateTemp);
            
            result.status = "S-200";
            result.message = "공지사항 수정 완료";
            result.data = null;

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            eresult.status = "E-4107";
            eresult.message = "서버 내부 오류로 인해 공지사항 수정 실패";
            eresult.data = null;

            errorMap.put("field", "updateNotice");
            errorMap.put("data", notice);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
