package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.QnaAnswerDao;
import com.blsa.ezilog.dao.QnaQuestionDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.notice.Notice;
import com.blsa.ezilog.model.qna.QnaQuestion;
import com.blsa.ezilog.model.qna.QnaQuestionRequest;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/qna")
public class QnaController {
    
    @Autowired
    QnaAnswerDao answerDao;
    
    @Autowired
    QnaQuestionDao questionDao;
    
    @PostMapping("/question")
    @ApiOperation(value ="질문 게시판 질문 생성", notes="QnaQuestionRequest를 이용하여 질문 생성")
    public Object createQuestion(@RequestBody QnaQuestionRequest qrequest) {
        ResponseEntity response = null;
        
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();
        
        try {
            QnaQuestion qnaQ = new QnaQuestion(qrequest.getTitle(), qrequest.getContent(), qrequest.getWriter());
            
            questionDao.save(qnaQ);
            result.status = "S-200";
            result.message = "성공적으로 Qna 질문 작성 완료";
            result.data = null;
            
            response = new ResponseEntity<>(result, HttpStatus.OK);
            
        }catch(Exception e) {
            
            eresult.status = "E-4300";
            eresult.message = "서버 내부 오류로 인해 Qna 질문 생성 실패.";
            eresult.data = null;
            errorMap.put("field", "createQna");
            errorMap.put("data", e.getMessage());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        
        return response;
        
    }
    
    @GetMapping("/question")
    @ApiOperation(value="질문 게시판 글 목록 불러오기", notes="Input : Page 번호 Output: page에 맞는 10개의 질문 글")
    public Object retrieveQuestion(@RequestParam int page) {
        ResponseEntity response = null;
        
        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "qid");
        
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();
        
        try {
            
            Page<QnaQuestion> qList = questionDao.findAll(pageable);
            if (!qList.isEmpty()) {
                result.status = "S-200";
                result.message = "공지사항 목록 불러오기에 성공했습니다.";
                result.data = qList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "S-204";
                eresult.message = "불러 올 공지사항이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noticeEmpty");
                errorMap.put("data", page);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
            }
            
        }catch(Exception e) {
            
        }
        
        
        
        return response;
    }
}
