package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.SelectionPostDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.post.SelectMainPostRequestDTO;
import com.blsa.ezilog.model.post.SelectionPost;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/selectionPost")
public class SelectionPostController {

    @Autowired
    SelectionPostDao selectionPostDao;
    
    @PostMapping
    @ApiOperation(value = "메인 고민으로 선정", notes = "기존 작성된 글 중 선택한 글을 메인에 선정, SelectMainPostRequestDTO를 이용하여 추가")
    public Object selectMainPost(@RequestBody SelectMainPostRequestDTO request) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();

        Optional<SelectionPost> mainPost = selectionPostDao.findByNo(request.getNo());
        if(mainPost.isPresent()) {
            errors.put("field", "no");
            errors.put("data", request.getNo());
            final ErrorResponse result = new ErrorResponse();
            result.status = "미정,,,,";
            result.message = "이미 메인에 선정된 글입니다.";
            result.errors = errors;

            response = new ResponseEntity<>(result, HttpStatus.CONFLICT);
        } else {
            SelectionPost post = new SelectionPost();
            post.setNo(request.getNo());
            post.setDescription(request.getDescription());
            
            selectionPostDao.save(post);
            
            final BasicResponse result = new BasicResponse();

            result.status = "S-200";
            result.message = "메인 선정에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        
        return response;
    }
    
    @GetMapping
    @ApiOperation(value = "선정된 메인 고민 목록 가져오기")
    public Object retrieveMainPost(@RequestBody SelectMainPostRequestDTO request) {

    }
    
    @DeleteMapping
    @ApiOperation(value = "메인 고민 선정 취소?.. 내리기.. 히스토리로 이동")
    public Object cancelMainPost(@RequestBody SelectMainPostRequestDTO request) {

    }
}
