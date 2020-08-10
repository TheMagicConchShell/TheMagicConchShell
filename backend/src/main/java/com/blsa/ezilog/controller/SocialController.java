package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.config.util.BCryptImpl;
import com.blsa.ezilog.dao.SnsDao;
import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.user.Sns;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.service.JwtService;
import com.blsa.ezilog.service.UserService;

@RestController
@RequestMapping("/sns")
public class SocialController {
    @Autowired
    private SnsDao snsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptImpl bcryptimpl;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public Object login(@RequestParam String id, HttpServletResponse res) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();

        Optional<Sns> sns = snsDao.findBySnsId(id);
        if (sns.isPresent()) {
            final BasicResponse result = new BasicResponse();
            Long uid = sns.get().getUid();
            User user = userDao.findByUid(uid).get();
            String token = jwtService.create(user);
            res.setHeader("jwt-auth-token", token);
            res.setHeader("nickname", user.getNickname());
            result.status = "S-200";
            result.message = "소셜 로그인에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            errors.put("field", "id");
            errors.put("data", id);
            final ErrorResponse result = setErrors("E-4501", "회원정보가 없습니다. 닉네임을 등록하여 회원가입을 진행해주세요", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/post")
    public Object post(@RequestParam String id, @RequestParam String nickname,
            @RequestParam(required = false) String email) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User user = new User();
        String checkname = userService.duplicateCheck("", nickname);
        String authTableCheckname = userService.authDuplicateCheck("", nickname);
        if ((checkname.equals("nickname") || authTableCheckname.equals("nickname"))) {
            errors.put("field", "nickname");
            errors.put("data", nickname);
            final ErrorResponse result = setErrors("E-4001", "이미 존재하는 별명 입니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.CONFLICT);
            return response;
        } else if (email != null) {
            String checkEmail = userService.duplicateCheck(email, "");
            String authTableCheckEmail = userService.authDuplicateCheck(email, "");
            if (checkEmail.equals("email") || authTableCheckEmail.equals("email")) {
                errors.put("field", "email");
                errors.put("data", email);
                final ErrorResponse result = setErrors("E-4000", "이미 존재하는 이메일 입니다.", errors);

                response = new ResponseEntity<>(result, HttpStatus.CONFLICT);
                return response;
            } else {
                user.setEmail(email);
            }
        }else {
            user.setEmail(id+"@kakao.com");
        }
        final BasicResponse result = new BasicResponse();
        user.setNickname(nickname);
        user.setSocialAccount(true);
        String temppw = "temp1234";
        user.setPassword(bcryptimpl.encrypt(temppw));
        user = userDao.save(user);
        Sns sns = new Sns();
        sns.setSnsId(id);
        sns.setUid(user.getUid());
        snsDao.save(sns);
        result.status = "S-201";
        result.message = "소셜 로그인 연동 회원가입에 성공했습니다.";
        response = new ResponseEntity<>(result, HttpStatus.CREATED);

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
