package com.blsa.ezilog.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        String authTableCheck = userService.authDuplicateCheck(request.getEmail(), request.getNickname());
        if (check.equals("email") || authTableCheck.equals("email")) {
            errors.put("field", "email");
            errors.put("data", request.getEmail());
            final ErrorResponse result = setErrors("E-4001", "이미 존재하는 이메일 입니다.", errors);

            response = new ResponseEntity<>(result, HttpStatus.CONFLICT);
        } else if (check.equals("nickname") || authTableCheck.equals("nickname")) {
            errors.put("field", "nickname");
            errors.put("data", request.getNickname());
            final ErrorResponse result = setErrors("E-4001", "이미 존재하는 별명 입니다.", errors);

            response = new ResponseEntity<>(result, HttpStatus.CONFLICT);
        } else {
            String token = mailSendService.getKey(false, 20);

            UserAuth u = userService.signup(request, token);
            try {
                mailSendService.mailSendWithUserKey(u.getEmail(), u.getNickname(), token, u.getAid());
            } catch (MessagingException e) {
                errors.put("field", "sendMail");

                final ErrorResponse result = setErrors("E-4007", "메일 발송에 실패했습니다.", errors);

                return new ResponseEntity<>(result, HttpStatus.CONFLICT);
            }
            final BasicResponse result = new BasicResponse();

            result.status = "S-200";
            result.message = "회원가입에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.CREATED);
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
            errors.put("field", "password");
            errors.put("data", request);
            final ErrorResponse result = setErrors("E-4003", "비밀번호가 일치하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else if (u.equals("email")) {
            errors.put("field", "email");
            errors.put("data", request);
            final ErrorResponse result = setErrors("E-4002", "존재하지 않는 이메일 입니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            String token = jwtService.create((User) u);
            res.setHeader("jwt-auth-token", token);
            res.setHeader("nickname", ((User) u).getNickname());
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "로그인에 성공했습니다.";
            result.data = u;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PutMapping("/user/update")
    @ApiOperation(value = "회원 정보 수정")
    public Object update(@Valid @RequestBody UpdateRequestDTO request, HttpServletResponse res) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User checkUser = userService.select(request.getUid());
        request.setEmail(checkUser.getEmail());
        String checkname = userService.duplicateCheck("", request.getNickname());
        String authTableCheckname = userService.authDuplicateCheck("", request.getNickname());
        if (!checkUser.getNickname().equals(request.getNickname())
                && (checkname.equals("nickname") || authTableCheckname.equals("nickname"))) {
            errors.put("field", "nickname");
            errors.put("data", request.getNickname());
            final ErrorResponse result = setErrors("E-4001", "이미 존재하는 별명 입니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.CONFLICT);

        } else {
            final BasicResponse result = new BasicResponse();
            User user = userService.update(request);
            String token = jwtService.create(user);
            res.setHeader("jwt-auth-token", token);
            res.setHeader("nickname", user.getNickname());
            result.status = "S-200";
            result.message = "회원 정보 수정이 완료되었습니다.";
            response = new ResponseEntity<>(result, HttpStatus.CREATED);
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
            errors.put("field", "uid");
            errors.put("data", uid);
            final ErrorResponse result = setErrors("E-4005", "번호에 해당하는 유저가 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "회원 정보 조회에 성공했습니다.";
            result.data = user;
            response = new ResponseEntity<>(result, HttpStatus.OK);
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
            errors.put("field", "uid");
            errors.put("data", uid);
            final ErrorResponse result = setErrors("E-4005", "번호에 해당하는 유저가 존재하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            userService.withdraw(uid);
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "회원 탈퇴에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @GetMapping("/user/authentication")
    @ApiOperation(value = "이메일 인증 완료")
    public Object confirm(@RequestParam long aid, @RequestParam String token) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        User user = userService.authentication(aid, token);
        if (user == null) {
            errors.put("field", "token");
            errors.put("data", token);
            final ErrorResponse result = setErrors("E-4008", "이메일 인증에 실패했습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "이메일 인증에 성공했습니다.";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/user/findpw")
    @ApiOperation(value = "비밀 번호 찾기")
    public Object findpw(@Email @NotBlank @RequestParam String email, @NotBlank @RequestParam String nickname) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();
        String pw = userService.findPw(email, nickname);
        if (pw.equals("email")) {
            errors.put("data", email);
            final ErrorResponse result = setErrors("E-4002", "존재하지 않는 이메일 입니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else if (pw.equals("nickname")) {
            errors.put("field", "nickname");
            errors.put("data", nickname);
            final ErrorResponse result = setErrors("E-4004", "닉네임이 일치하지 않습니다.", errors);
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            try {
                mailSendService.mailSendWithPassword(email, nickname, pw);
            } catch (MessagingException e) {
                errors.put("field", "sendMail");
                final ErrorResponse result = setErrors("E-4007", "메일 발송에 실패했습니다.", errors);
                return new ResponseEntity<>(result, HttpStatus.CONFLICT);
            }
            final BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "비밀번호 찾기에 성공했습니다.";
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
