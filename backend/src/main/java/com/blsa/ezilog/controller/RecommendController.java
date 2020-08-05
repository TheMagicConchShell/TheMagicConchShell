package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.service.RecommendService;

import io.swagger.annotations.ApiOperation;

@RestController
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping("/post/recommend/auto")
    @ApiOperation(value = "고민 글 자동 추천 영역 리스트 반환")
    public Object RecommendAuto() {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();

        if (recommendService.getRecommendPosts().size() == 0 || (recommendService.getRecommendPosts() == null)) {
            errors.put("field", "Post");
            errors.put("data", null);
            final ErrorResponse result = setErrors("E-4500", "추천할 고민 글이 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.data = recommendService.getRecommendPosts();
            result.message = "추천할 고민 글 반환 성공.";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/post/recommend")
    @ApiOperation(value = "고민 글 추천 영역 리스트 반환")
    public Object RecommendPost() {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        recommendService.calculate();
        if (recommendService.getRecommendPosts().size() == 0 || (recommendService.getRecommendPosts() == null)) {
            errors.put("field", "Post");
            errors.put("data", null);
            final ErrorResponse result = setErrors("E-4500", "추천할 고민 글이 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.data = recommendService.getRecommendPosts();
            result.message = "추천할 고민 글 반환 성공.";
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
