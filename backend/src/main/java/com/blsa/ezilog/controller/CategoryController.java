package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.CategoryDao;
import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.exception.auth.ForbiddenException;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.category.Category;
import com.blsa.ezilog.model.category.CategoryCreateRequest;
import com.blsa.ezilog.model.category.CategoryUpdateRequest;
import com.blsa.ezilog.model.user.User;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    CategoryDao categoryDao;
    
    @Autowired
    UserDao userDao;
    
    @ApiOperation(value = "카테고리 목록 가져오기", 
            notes = "output: 전체 가테고리 리스트", 
            response = Category.class, 
            responseContainer = "List")
    @GetMapping
    public ResponseEntity<BasicResponse> retrieveCategory() {
        ResponseEntity<BasicResponse> response = null;
        final BasicResponse result = new BasicResponse();

        List<Category> categoryList = categoryDao.findAll();

        result.status = "S-200";
        result.message = "카테고리 목록 불러오기에 성공했습니다.";
        result.data = categoryList;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        
        return response;
    }

    @ApiOperation(value = "카테고리 생성", notes = "현재 로그인 유저 닉네임, CategoryRequest")
    @PostMapping
    public Object createCategory(@RequestBody CategoryCreateRequest crequest,
            @RequestHeader(value = "nickname", required = false) String nickname) throws ForbiddenException {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            if (nickname.equals("admin")) {
                Optional<Category> optCate = categoryDao.findCategoryByName(crequest.getName());

                if (!optCate.isPresent()) {

                    Category temp = new Category(crequest.getName(), crequest.getDescription(), crequest.getEnName(), crequest.getChName());
                    categoryDao.save(temp);

                    result.status = "S-200";
                    result.message = "카테고리 생성에 성공했습니다.";
                    result.data = crequest.getName();
                    response = new ResponseEntity<>(result, HttpStatus.CREATED);
                } else {
                    eresult.status = "E-4427";
                    eresult.message = "이미 카테고리가 있습니다.";
                    eresult.data = null;
                    errorMap.put("field", "existCategory");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                }
            } else {
                throw new ForbiddenException(
                        "/category/create", 
                        "카테고리를 만들 수 있는 권한이 없습니다.", 
                        "administrator", 
                        nickname);
            }
        } else {
            eresult.status = "E-4409";
            eresult.message = "알수 없는 유저입니다. 카테고리를 생성 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "카테고리 삭제", notes = "Input : 지우려는 카테고리 이름, 현재 로그인 되어있는 사람 Output: 성공 - null, 실패 : 중복시 카테고리 이름 반환")
    @DeleteMapping
    public Object deleteCategory(@RequestParam String categoryName,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            if (nickname.equals("admin")) {
                Optional<Category> optCate = categoryDao.findCategoryByName(categoryName);

                if (optCate.isPresent()) {
                    Category ctemp = optCate.get();
                    categoryDao.delete(ctemp);

                    result.status = "S-200";
                    result.message = "카테고리 삭제에 성공했습니다.";
                    result.data = categoryName;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4429";
                    eresult.message = "지우려는 카테고리가 존재하지 않습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noCategory");
                    errorMap.put("data", categoryName);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4430";
                eresult.message = "카테고리를 지울 수 있는 권한이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deleteCategory");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4409";
            eresult.message = "알수 없는 유저입니다. 카테고리를 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "카테고리 내용 변경", notes = "input CategoryRequest, 현재 로그인 닉네임 output : 성공 여부")
    @PutMapping
    public Object updateCategory(@RequestBody CategoryUpdateRequest urequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);

        if (!userOpt.isPresent()) {
            eresult.status = "E-4409";
            eresult.message = "알수 없는 유저입니다. 카테고리를 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unkownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        } else {
            if (nickname.equals("admin")) {

                Optional<Category> cateOpt = categoryDao.findCategoryByName(urequest.getCurName());

                if (cateOpt.isPresent()) {

                    Category ctemp = cateOpt.get();
                    ctemp.setName(urequest.getChangeName());
                    ctemp.setDescription(urequest.getDescription());
                    ctemp.setEnName(urequest.getEnName());
                    ctemp.setChName(urequest.getChName());
                    System.out.println(ctemp.toString());
                    categoryDao.save(ctemp);
                    

                    result.status = "S-200";
                    result.message = "카테고리 수정에 성공했습니다.";
                    result.data = urequest.getChangeName();
                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    eresult.status = "E-4431";
                    eresult.message = "수정하려는 카테고리가 존재하지 않습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noCategory");
                    errorMap.put("data", urequest.getCurName());
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4432";
                eresult.message = "카테고리를 수정 할 수 있는 권한이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updateCategory");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        }

        return response;
    }

}
