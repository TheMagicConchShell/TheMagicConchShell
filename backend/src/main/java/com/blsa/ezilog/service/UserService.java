package com.blsa.ezilog.service;

import com.blsa.ezilog.model.user.LoginRequestDTO;
import com.blsa.ezilog.model.user.SignupRequestDTO;
import com.blsa.ezilog.model.user.UpdateRequestDTO;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.model.user.UserAuth;

public interface UserService {
    UserAuth signup(SignupRequestDTO request, String token);

    Object login(LoginRequestDTO request);

    User update(UpdateRequestDTO request, String nickname);

    User select(String nickname);

    void withdraw(String nickname);

    String duplicateCheck(String email, String nickname);

    String authDuplicateCheck(String email, String nickname);

    User authentication(long uaid, String token);

    String findPw(String email, String nickname);
}
