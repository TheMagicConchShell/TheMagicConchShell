package com.blsa.ezilog.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.BlogDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.blog.Blog;
import com.blsa.ezilog.model.blog.BlogRequest;
import com.blsa.ezilog.model.blog.BlogUpdateRequest;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogDao blogDao;

    @PostMapping
    @ApiOperation(value = "블로그 개설")
    public Object createBlog(@RequestBody BlogRequest blogCreate) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();
        try {
            Optional<Blog> blogName = blogDao.getBlogByName(blogCreate.getName());
            Optional<Blog> blogUrl = blogDao.getBlogByUrl(blogCreate.getUrl());

            if (blogName.isPresent()) {
                // 동일한 블로그 이름이 있는지 체크
                eresult.status = "E-4100";
                eresult.message = "이미 사용 중인 블로그 이름입니다.";
                eresult.data = null;
                errorMap.put("field", "name");
                errorMap.put("data", blogCreate.getName());

                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
            } else if (blogUrl.isPresent()) {
                // 동일한 블로그 url에 있는지 체크
                eresult.status = "E-4101";
                eresult.message = "이미 사용 중인 블로그 URL입니다.";
                eresult.data = null;
                errorMap.put("field", "url");
                errorMap.put("data", blogCreate.getUrl());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
            } else {
                LocalDateTime currentTime = LocalDateTime.now();
                Blog b = new Blog(blogCreate.getName(), blogCreate.getUrl(), currentTime);

                blogDao.save(b);
                result.status = "S-200";
                result.message = "블로그 생성에 성공했습니다.";
                result.data = null;
                response = new ResponseEntity<>(result, HttpStatus.OK);

            }
        } catch (Exception e) {
            eresult.status = "E-4102";
            eresult.message = "서버 내부 오류로 인해 블로그 생성 실패.";
            eresult.data = null;
            errorMap.put("field", "createBlog");
            errorMap.put("data", e.getMessage());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;

    }

    @GetMapping
    @ApiOperation(value = "전체 블로그 목록 가져오기", notes = "전체 블로그 목록 반환")
    public Object retrieveAll(@RequestParam int page) {

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "id");

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Page<Blog> blogList = blogDao.findAll(pageable);
            if (!blogList.isEmpty()) {
                result.status = "S-200";
                result.message = "블로그 목록들 가져오기 성공";
                result.data = blogList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "S-204";
                eresult.message = "불러 올 블로그가  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noBlog");
                errorMap.put("data", page);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            eresult.status = "E-4103";
            eresult.message = "서버 내부 오류에 의해 블로그 목록 가져오기 실패";
            eresult.data = null;

            errorMap.put("field", "getBlogList");
            errorMap.put("data", e.getMessage());
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/name")
    @ApiOperation(value = "이름으로 블로그 검색", notes = "블로그 이름을 받아서 해당 블로그 반환")
    public Object SeatchByName(@RequestParam String blogName) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Optional<Blog> blogOpt = blogDao.getBlogByName(blogName);
            if (blogOpt.isPresent()) {

                Blog b = blogOpt.get();
                result.status = "S-200";
                result.message = "이름에 해당 되는 블로그 가져오기 성공";
                result.data = b;
                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "S-204";
                eresult.message = "불러 올 블로그가  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noBlog");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            eresult.status = "E-4104";
            eresult.message = "서버 내부 오류로 인한 이름에 해당 되는 블로그 가져오기 실패";
            eresult.data = null;

            errorMap.put("field", "getBlogByName");
            errorMap.put("data", blogName);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/url")
    @ApiOperation(value = "URL으로 블로그 검색", notes = "블로그 URL을 받아서 해당 블로그 반환")
    public Object SeatchByUrl(@RequestParam String blogURL) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Optional<Blog> blogOpt = blogDao.getBlogByUrl(blogURL);

            if (blogOpt.isPresent()) {
                Blog b = blogOpt.get();

                result.status = "S-200";
                result.message = "Url에 해당 되는 블로그 가져오기 성공";
                result.data = b;
                response = new ResponseEntity<>(result, HttpStatus.OK);
                
            }else {
                eresult.status = "S-204";
                eresult.message = "불러 올 블로그가  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noBlog");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            eresult.status = "E-4105";
            eresult.message = "서버 내부 오류로 인한 URL에 해당 되는 블로그 가져오기 실패";
            eresult.data = null;

            errorMap.put("field", "getBlogByURL");
            errorMap.put("data", blogURL);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @PutMapping
    @ApiOperation(value = "블로그 정보 변경", notes = "BlogUpdateRequest DTO를 보내면 이를 변경 하도록 만든다")
    public Object updateBlog(@RequestBody BlogUpdateRequest updateBlog) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Optional<Blog> updateOpt = blogDao.getBlogById(updateBlog.getId());
            Blog b = updateOpt.get();

            b.setName(updateBlog.getName());
            b.setUrl(updateBlog.getUrl());

            blogDao.save(b);
            result.status = "S-200";
            result.message = "블로그 정보 수정 완료";
            result.data = null;

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            eresult.status = "E-4106";
            eresult.message = "서버 내부 오류로 인해 블로그 정보 수정 실패";
            eresult.data = null;

            errorMap.put("field", "updateBlog");
            errorMap.put("data", updateBlog);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @DeleteMapping
    @ApiOperation(value = "블로그 정보 삭제", notes = "Blog 이름 명을 통해서 블로그 삭제")
    public Object updateBlog(@RequestBody String blogName) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            Optional<Blog> updateOpt = blogDao.getBlogByName(blogName);
            Blog b = updateOpt.get();

            blogDao.delete(b);
            result.status = "S-200";
            result.message = "블로그 정보 삭제 완료";
            result.data = null;

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            eresult.status = "E-4107";
            eresult.message = "서버 내부 오류로 인해 블로그 정보 삭제 실패";
            eresult.data = null;

            errorMap.put("field", "deleteBlog");
            errorMap.put("data", blogName);
            response = new ResponseEntity<>(eresult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
