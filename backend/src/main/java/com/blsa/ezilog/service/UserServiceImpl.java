package com.blsa.ezilog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.user.LoginRequestDTO;
import com.blsa.ezilog.model.user.SignupRequestDTO;
import com.blsa.ezilog.model.user.UpdateRequestDTO;
import com.blsa.ezilog.model.user.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Override
    public User signup(SignupRequestDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());

        return dao.save(user);
    }

    @Override
    public Object login(LoginRequestDTO request) {
        Object res = null;
        Optional<User> user = dao.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user.isPresent()) {
            res = user.get();
        } else {
            Optional<User> email = dao.findByEmail(request.getEmail());
            if (email.isPresent()) {
                res = "password";
            } else {
                res = "email";
            }
        }
        return res;
    }

    @Override
    public String duplicateCheck(String email, String nickname) {
        String res = null;
        if (dao.findByEmail(email).isPresent()) {
            res = "email";
        } else if (dao.findByNickname(nickname).isPresent()) {
            res = "nickname";
        } else {
            res = "ok";
        }
        return res;

    }

    @Override
    public User update(UpdateRequestDTO request) {
        User user = new User();
        user.setUid(request.getUid());
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setPassword(request.getPassword());
        user.setIntroduce(request.getIntroduce());
        user.setProfileImg(request.getProfileImg());
        user = dao.save(user);
        return user;
    }

    @Override
    public User select(long uid) {
        return dao.findByUid(uid).orElse(null);
    }

    @Override
    public void withdraw(long uid) {
        dao.deleteById(uid);
    }

}
