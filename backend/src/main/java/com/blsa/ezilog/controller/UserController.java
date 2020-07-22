package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.user.LoginRequestDTO;
import com.blsa.ezilog.model.user.SignupRequestDTO;
import com.blsa.ezilog.model.user.UpdateRequestDTO;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.model.user.UserAuth;
import com.blsa.ezilog.service.JwtService;
import com.blsa.ezilog.service.MailSendService;
import com.blsa.ezilog.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = ErrorResponse.class) })

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MailSendService mailSendService;

    @PostMapping("/user/signup")
    @ApiOperation(value = "회원 가입")
    public Object signup(@Valid @RequestBody SignupRequestDTO request) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        String check = userService.duplicateCheck(request.getEmail(), request.getNickname());
        if (check.equals("email")) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4000";
            result.message = "이미 존재하는 이메일 입니다.";
            errors.put("field", "email");
            errors.put("data", request.getEmail());
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else if (check.equals("nickname")) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4001";
            result.message = "이미 존재하는 별명 입니다.";
            errors.put("field", "nickname");
            errors.put("data", request.getNickname());
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            String key = mailSendService.getKey(false, 20);

            UserAuth u = userService.signup(request, key);
            try {
                mailSendService.mailSendWithUserKey(u.getEmail(), u.getNickname(), key, u.getUaid());
            } catch (MessagingException e) {
                final ErrorResponse result = new ErrorResponse();
                result.status = "E-4006";
                result.message = "인증 메일 발송에 실패했습니다.";
                errors.put("field", "sendMail");

                result.errors = errors;
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            final BasicResponse result = new BasicResponse();

            result.status = "S-200";
            result.message = "회원가입에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/user/login")
    @ApiOperation(value = "로그인")
    public Object login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse res) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        Object u = userService.login(request);
        if (u.equals("password")) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4003";
            result.message = "로그인에 실패했습니다.(비밀번호가 일치하지 않습니다.)";
            errors.put("field", "password");
            errors.put("data", request);
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else if (u.equals("email")) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4002";
            result.message = "로그인에 실패했습니다.(존재하지 않는 이메일 입니다.)";
            errors.put("field", "email");
            errors.put("data", request);
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            String token = jwtService.create((User) u);
            res.setHeader("jwt-auth-token", token);
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "로그인에 성공했습니다.";
            result.data = u;
            response = new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        }
        return response;
    }

    @PutMapping("/user/update")
    @ApiOperation(value = "회원 정보 수정")
    public Object update(@Valid @RequestBody UpdateRequestDTO request) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User checkUser = userService.select(request.getUid());
        String checkname = userService.duplicateCheck("", request.getNickname());
        if (!checkUser.getNickname().equals(request.getNickname()) && checkname.equals("nickname")) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4001";
            result.message = "이미 존재하는 별명 입니다.";
            errors.put("field", "nickname");
            errors.put("data", request.getNickname());
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        } else {
            final BasicResponse result = new BasicResponse();
            userService.update(request);
            result.status = "S-200";
            result.message = "회원 정보 수정이 완료되었습니다.";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/user/detail")
    @ApiOperation(value = "회원 정보 조회")
    public Object select(@RequestParam long uid) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User user = userService.select(uid);
        if (user == null) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4004";
            result.message = "번호에 해당하는 유저가 존재하지 않습니다.";
            errors.put("field", "uid");
            errors.put("data", uid);
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "회원 정보 조회에 성공했습니다.";
            result.data = user;
            response = new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        }
        return response;
    }

    @DeleteMapping("/user/delete")
    @ApiOperation(value = "회원 탈퇴")
    public Object withdraw(@RequestParam long uid) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User user = userService.select(uid);
        if (user == null) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4004";
            result.message = "번호에 해당하는 유저가 존재하지 않습니다.";
            errors.put("field", "uid");
            errors.put("data", uid);
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            userService.withdraw(uid);
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "회원 탈퇴에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        }
        return response;
    }

    @PostMapping("/user/authentication")
    @ApiOperation(value = "이메일 인증 완료")
    public Object confirm(@RequestParam long uid, @RequestParam String token) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User user = userService.authentication(uid, token);
        if (user == null) {
            final ErrorResponse result = new ErrorResponse();
            result.status = "E-4007";
            result.message = "이메일 인증에 실패했습니다.";
            errors.put("field", "token");
            errors.put("data", token);
            result.errors = errors;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "이메일 인증에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        }
        return response;
    }
}
