package com.blsa.ezilog.controller;

import java.math.BigInteger;
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.AnswerDao;
import com.blsa.ezilog.dao.QuestionDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.qna.Answer;
import com.blsa.ezilog.model.qna.AnswerRequest;
import com.blsa.ezilog.model.qna.AnswerUpdateRequest;
import com.blsa.ezilog.model.qna.Question;
import com.blsa.ezilog.model.qna.QuestionRequest;
import com.blsa.ezilog.model.qna.QuestionUpdateRequest;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.service.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/qna")
public class QnaController {

    @Autowired
    AnswerDao answerDao;

    @Autowired
    QuestionDao questionDao;
    
    @Autowired
    UserService userservice;

    @PostMapping("/question")
    @ApiOperation(value = "질문 게시판 질문 생성", notes = "QnaQuestionRequest를 이용하여 질문 생성")
    public Object createQuestion(@RequestBody QuestionRequest qrequest) {
        ResponseEntity response = null;

        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        
        LocalDateTime currentTime = LocalDateTime.now();
        Question qnaQ = new Question(qrequest.getTitle(), qrequest.getContent(), qrequest.getWriter(), currentTime);

        questionDao.save(qnaQ);
        result.status = "S-200";
        result.message = "성공적으로 Qna 질문 작성 완료";
        result.data = null;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;

    }

    @PostMapping("/answer")
    @ApiOperation(value = "질문 게시판 답변 생성", notes = "AnswerRequest를 이용하여 질문 생성")
    public Object createAnswer(@RequestBody AnswerRequest qrequest) {
        ResponseEntity response = null;

        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Question> questionOpt = questionDao.getQuestionByNo(qrequest.getQid());

        if (questionOpt.isPresent()) {
            LocalDateTime currentTime = LocalDateTime.now();

            Answer qnaQ = new Answer(qrequest.getContent(), qrequest.getWriter(), qrequest.getQid(), currentTime);

            answerDao.save(qnaQ);
            result.status = "S-200";
            result.message = "성공적으로  질문 답변 작성 완료";
            result.data = null;

            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        else {
            eresult.status = "E-4300";
            eresult.message = "답변 작성을 위한 질문이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @GetMapping("/question")
    @ApiOperation(value = "질문 게시판 글 목록 불러오기", notes = "Input : Page 번호 Output: page에 맞는 10개의 질문 글")
    public Object retrieveQuestion(@RequestParam int page) {
        ResponseEntity response = null;

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");

        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Page<Question> qList = questionDao.findAll(pageable);
        if (!qList.isEmpty()) {
            result.status = "S-200";
            result.message = "질문 목록 불러오기에 성공했습니다.";
            result.data = qList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4301";
            eresult.message = "불러 올 질문 목록이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }
    
    @GetMapping("/question/no")
    @ApiOperation(value = "질문 정보 불러오기")
    public Object getQuestion(@RequestParam Long no) {
        ResponseEntity response = null;

 
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Question> qOpt = questionDao.findByNo(no);
        if (qOpt.isPresent()) {
            Question question = qOpt.get();
            List<Answer> answers = answerDao.findAllByQid(no);
            Map<String, Object> postMap = new HashMap<String, Object>();
            result.status = "S-200";
            result.message = "질문 정보 불러오기에 성공했습니다.";
            postMap.put("question", question);
            postMap.put("answers", answers);
            result.data = postMap;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4301";
            eresult.message = "불러 올 질문이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/question/writer")
    @ApiOperation(value = "질문 게시판 글 목록 불러오기", notes = "Input : Page 번호, 작성자, Output: page에 맞는 10개의 질문 글")
    public Object retrieveQuestionByWriter(@RequestParam int page, @RequestParam String writer) {
        ResponseEntity response = null;

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "no");

        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Page<Question> qList = questionDao.findQuestionByWriter(writer, pageable);
        if (!qList.isEmpty()) {
            result.status = "S-200";
            result.message = "질문 목록 불러오기에 성공했습니다.";
            result.data = qList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4302";
            eresult.message = "해당 작성자가 작성한 질문들이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/question/title")
    @ApiOperation(value = "질문 게시판 글 목록 불러오기", notes = "Input : Page 번호, 제목 Output: page에 맞는 10개의 질문 글")
    public Object retrieveQuestionByTitle(@RequestParam int page, @RequestParam String title) {
        ResponseEntity response = null;

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "no");

        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Page<Question> qList = questionDao.findQuestionByTitleLike(title, pageable);
        if (!qList.isEmpty()) {
            result.status = "S-200";
            result.message = "질문 목록 불러오기에 성공했습니다.";
            result.data = qList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4303";
            eresult.message = "해당 제목을 가진 질문들이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/answer")
    @ApiOperation(value = "질문에 달아둔 답변들 가져오기", notes = "Input : 질문 번호, page, Output : 답변 목록")
    public Object retrieveAnswer(@RequestParam Long qid) {
        ResponseEntity response = null;

        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        List<Answer> aList = answerDao.findAllByQid(qid);
        if (!aList.isEmpty()) {
            result.status = "S-200";
            result.message = "답변 목록 불러오기에 성공했습니다.";
            result.data = aList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4304";
            eresult.message = "해당 질문에 대한 답변이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "answerEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PutMapping("/question")
    @ApiOperation(value = "질문 글 수정", notes = "Input : 수정 QuestionUpdateRequest, Output : 성공 여부 메세지")
    public Object updateQuestion(@RequestBody QuestionUpdateRequest uprequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();
        Optional<Question> qtemp = questionDao.getQuestionByNo(uprequest.getNo());

        if (qtemp.isPresent()) {
            Question temp = qtemp.get();
            if(nickname==null||!nickname.equals(temp.getWriter())) {
                eresult.status = "E-4309";
                eresult.message = "작성한 작성자가 아니면 수정할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "writer");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            }else {
                temp.setTitle(uprequest.getTitle());
                temp.setContent(uprequest.getContent());
                questionDao.save(temp);
    
                result.status = "S-200";
                result.message = "질문 사항 수정 완료";
                result.data = null;
    
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }

        } else {
            eresult.status = "E-4305";
            eresult.message = "수정을 하기 위한 해당 질문이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PutMapping("/answer")
    @ApiOperation(value = "답변 수정", notes = "Input : 수정 answerUpdateRequest, Output : 성공 여부 메세지")
    public Object updateAnswer(@RequestBody AnswerUpdateRequest uprequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Answer> atemp = answerDao.getQnaAnswerByNo(uprequest.getNo());

        if (atemp.isPresent()) {
            Answer temp = atemp.get();
            if(nickname==null||!nickname.equals(temp.getWriter())) {
                eresult.status = "E-4309";
                eresult.message = "작성한 작성자가 아니면 수정할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "writer");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            }
            temp.setContent(uprequest.getContent());
            answerDao.save(temp);

            result.status = "S-200";
            result.message = "답변 사항 수정 완료";
            result.data = null;

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            eresult.status = "E-4306";
            eresult.message = "수정 답변에 해당하는 질문이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "answerEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @DeleteMapping("/qeuestion")
    @ApiOperation(value = "질문 삭제 삭제", notes = "Input : 해당 질문 no를 받아서 삭제")
    public Object deleteQuestion(@RequestParam Long no,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Question> questionOpt = questionDao.getQuestionByNo(no);

        if (questionOpt.isPresent()) {

            Question temp = questionOpt.get();
            if(nickname==null||!nickname.equals(temp.getWriter())) {
                eresult.status = "E-4309";
                eresult.message = "작성한 작성자가 아니면 수정할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "writer");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            }
            questionDao.delete(temp);

            result.status = "S-200";
            result.message = "질문 삭제 삭제 완료";
            result.data = null;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {

            eresult.status = "E-4307";
            eresult.message = "삭제 할 질문이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @DeleteMapping("/answer")
    @ApiOperation(value = "답변 삭제 삭제", notes = "Input : 해당 답변 no를 받아서 삭제")
    public Object deleteAnswer(@RequestParam Long no,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Answer> answerOpt = answerDao.getQnaAnswerByNo(no);

        if (answerOpt.isPresent()) {

            Answer temp = answerOpt.get();
            if(nickname==null||!nickname.equals(temp.getWriter())) {
                eresult.status = "E-4309";
                eresult.message = "작성한 작성자가 아니면 수정할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "writer");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            }
            answerDao.delete(temp);

            result.status = "S-200";
            result.message = "답변 삭제 삭제 완료";
            result.data = null;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {

            eresult.status = "E-4308";
            eresult.message = "삭제할 답변이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "questionEmpty");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

}
