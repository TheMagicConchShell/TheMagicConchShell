package com.blsa.ezilog.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.SelectionPostDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.post.SelectionPostRequestDTO;
import com.blsa.ezilog.model.post.SelectionHistory;
import com.blsa.ezilog.model.post.SelectionPost;
import com.blsa.ezilog.service.SelectionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/selection")
public class SelectionController {

    @Autowired
    SelectionPostDao selectionPostDao;
    
    @Autowired
    SelectionService selectionService;
    
    @PostMapping("/post")
    @ApiOperation(value = "메인 고민으로 선정", notes = "기존 작성된 글 중 선택한 글을 메인에 선정, SelectMainPostRequestDTO를 이용하여 추가")
    public Object selectMainPost(@Valid @RequestBody SelectionPostRequestDTO request) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        
        // to do: 노출 allow한 글인지 체크!!
        // to do2: 존재하는 post인지 체크!!

        Optional<SelectionPost> mainPost = selectionPostDao.findByNo(request.getNo());
        if(mainPost.isPresent()) {
            errors.put("field", "no");
            errors.put("data", request.getNo());
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4431";
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
            result.data = post;
            
            response = new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        
        return response;
    }
    
    @GetMapping("/post")
    @ApiOperation(value = "선정된 메인 고민 목록 가져오기", notes = "lastMainPostId: 현재까지 페이지에 그려진 고민글 no 중 가장 작은 값, size: 가져올 글의 개수 ")
    public Object retrieveMainPost(@RequestParam BigInteger lastMainPostId, @RequestParam int size) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        
        Pageable pageable = PageRequest.of(0, size);
        Page<SelectionPost> list = selectionPostDao.findByIdLessThanOrderByIdDesc(lastMainPostId, pageable);
        
        if(list.isEmpty()) {
            errors.put("field", "lastMainPostId");
            errors.put("data", lastMainPostId);
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4430";
            result.message = "불러올 메인 고민이  없습니다.";
            result.errors = errors;
            
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }else {
            final BasicResponse result = new BasicResponse();
            
            result.status = "S-200";
            result.message = "메인 고민 목록 조회에 성공했습니다.";
            result.data = list.getContent();
            
            response = new ResponseEntity<>(result, HttpStatus.OK);            
        }
        return response;
    }
    
    @DeleteMapping("/post")
    @ApiOperation(value = "메인 고민에서 내리기, 메인에 있던 글을 히스토리로 이동")
    public Object removeMainPost(@Valid @RequestBody SelectionPostRequestDTO request) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();

        SelectionHistory histoty = selectionService.removeMainPostandAddtoHistory(request);
        if(histoty == null) {
            errors.put("field", "no");
            errors.put("data", request.getNo());
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4432";
            result.message = "존재하지 않는 메인 글입니다.";
            result.errors = errors;

            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            final BasicResponse result = new BasicResponse();

            result.status = "S-200";
            result.message = "히스토리로 이동에 성공했습니다.";
            result.data = histoty;
            
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        
        return response;
    }
}
