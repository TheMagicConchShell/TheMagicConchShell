package com.blsa.ezilog.service;

import com.blsa.ezilog.model.user.LoginRequestDTO;
import com.blsa.ezilog.model.user.SignupRequestDTO;
import com.blsa.ezilog.model.user.UpdateRequestDTO;
import com.blsa.ezilog.model.user.User;

public interface UserService {
    User signup(SignupRequestDTO request);

    User login(LoginRequestDTO request);

    User update(UpdateRequestDTO request);

    User select(long uid);

    void withdraw(long uid);

    String duplicateCheck(String email, String nickname);
}
