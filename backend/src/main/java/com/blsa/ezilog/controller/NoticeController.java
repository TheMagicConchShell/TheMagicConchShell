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
import com.blsa.ezilog.model.notice.Notice;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/support/notice")
public class NoticeController {

    @Autowired
    NoticeDao noticeDao;

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
    public ResponseEntity<Map<String, Object>> seasrchNoticeById(@RequestParam BigInteger id) {

        ResponseEntity<Map<String, Object>> entity = null;

        try {
            Notice temp = noticeDao.getNoticeByNid(id);
            entity = handleSuccess(temp);

        } catch (Exception e) {
            e.printStackTrace();
            entity = handleException(e);
        }
        return entity;

    }

    @ApiOperation(value = "공지사항 작성", response = List.class)
    @PostMapping
    public ResponseEntity<Map<String, Object>> insertNotice(@RequestBody Notice notice) {

        ResponseEntity<Map<String, Object>> entity = null;

        try {
            Notice temp = new Notice(notice.getNid(), notice.getTitle(), notice.getContent(), notice.getWriter());
            noticeDao.save(temp);
            String result = "success";
            entity = handleSuccess(result);

        } catch (Exception e) {
            entity = handleException(e);
        }
        return entity;
    }

    @ApiOperation(value = "공지사항 삭제", response = List.class)
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteNotice(@RequestParam BigInteger nid) {

        ResponseEntity<Map<String, Object>> entity = null;

        try {
            Notice temp = noticeDao.getNoticeByNid(nid);
            noticeDao.delete(temp);
            String result = "success";
            entity = handleSuccess(result);

        } catch (Exception e) {
            entity = handleException(e);
        }
        return entity;
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

        } catch (Exception e) {
            entity = handleException(e);
        }

        return entity;
    }

    private ResponseEntity<Map<String, Object>> handleSuccess(Object data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", true);
        resultMap.put("data", data);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", false);
        resultMap.put("data", e.getMessage());
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
