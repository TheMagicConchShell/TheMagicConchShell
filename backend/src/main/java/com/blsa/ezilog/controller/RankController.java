package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.rank.PostRankDao;
import com.blsa.ezilog.dao.rank.ReplyRankDao;
import com.blsa.ezilog.dao.rank.UserRankDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.rank.PostRank;
import com.blsa.ezilog.model.rank.ReplyRank;
import com.blsa.ezilog.model.rank.UserRank;
import com.blsa.ezilog.model.reply.Reply;
import com.blsa.ezilog.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rank")
public class RankController {
    @Autowired
    private UserRankDao urDao;

    @Autowired
    private PostRankDao prDao;

    @Autowired
    private ReplyRankDao rrDao;

    @GetMapping("/user")
    @ApiOperation(value = "회원 랭킹 조회")
    public Object selectUser(@RequestParam Integer sort) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        List<UserRank> list = urDao.selectUserRankData(sort);
        if (list.size() == 0 || list == null) {
            errors.put("field", "User");
            errors.put("data", null);
            final ErrorResponse result = setErrors("E-4510", "회원 정보가 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "회원 랭킹 조회에 성공했습니다.";
            result.data = list;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/post")
    @ApiOperation(value = "고민 글 랭킹 조회")
    public Object selectPost(@RequestParam Integer sort) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        List<PostRank> list = prDao.selectPostRankData(sort);
        if (list.size() == 0 || list == null) {
            errors.put("field", "Post");
            errors.put("data", null);
            final ErrorResponse result = setErrors("E-4511", "고민 글이 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        } else {
            for (PostRank post : list) {
                if (post.isSecret()) {
                    post.setWriter("익명의 작성자");
                }
            }
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "고민 글 랭킹 조회에 성공했습니다.";
            result.data = list;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/reply")
    @ApiOperation(value = "답변 랭킹 조회")
    public Object selectReply() {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        List<ReplyRank> list = rrDao.selectReplyRankData();
        if (list.size() == 0 || list == null) {
            errors.put("field", "user");
            errors.put("data", null);
            final ErrorResponse result = setErrors("E-4512", "답변이 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        } else {
            for (ReplyRank reply : list) {
                if (reply.isSecret()) {
                    reply.setWriter("익명의 작성자");
                }
            }
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "답변 랭킹 조회에 성공했습니다.";
            result.data = list;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    private ErrorResponse setErrors(String status, String message, Map<String, Object> errors) {
        ErrorResponse res = new ErrorResponse();
        res.status = status;
        res.message = message;
        res.errors = errors;
        return res;
    }
}
