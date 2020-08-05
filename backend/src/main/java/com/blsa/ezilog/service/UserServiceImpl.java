package com.blsa.ezilog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.config.util.BCryptImpl;
import com.blsa.ezilog.dao.UserAuthDao;
import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.user.LoginRequestDTO;
import com.blsa.ezilog.model.user.SignupRequestDTO;
import com.blsa.ezilog.model.user.UpdateRequestDTO;
import com.blsa.ezilog.model.user.User;
import com.blsa.ezilog.model.user.UserAuth;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Autowired
    private UserAuthDao authDao;
    
    @Autowired
    private BCryptImpl bcryptimpl;

    @Override
    public UserAuth signup(SignupRequestDTO request, String token) {
        UserAuth user = new UserAuth();
        user.setEmail(request.getEmail());
        String encrypted = bcryptimpl.encrypt(request.getPassword());
        user.setPassword(encrypted);
        System.out.println(user.getPassword());
        user.setNickname(request.getNickname());
        user.setToken(token);

        return authDao.save(user);
    }

    @Override
    public Object login(LoginRequestDTO request) {
        Object res = null;
        
        Optional<User> userOpt = dao.findByEmail(request.getEmail());
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println(user.getPassword());
            System.out.println(request.getPassword());
            
            if(bcryptimpl.isMatch(request.getPassword(), user.getPassword())) {
                res = user;
            }else {
                res = "password";
            }
        }else {
            res = "email";
        }
//        Optional<User> user = dao.findByEmailAndPassword(request.getEmail(), request.getPassword());
//        if (user.isPresent()) {
//            res = user.get();
//        } else {
//            Optional<User> email = dao.findByEmail(request.getEmail());
//            if (email.isPresent()) {
//                res = "password";
//            } else {
//                res = "email";
//            }
//        }
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
    public String authDuplicateCheck(String email, String nickname) {
        String res = null;
        if (authDao.findByEmail(email).isPresent()) {
            res = "email";
        } else if (authDao.findByNickname(nickname).isPresent()) {
            res = "nickname";
        } else {
            res = "ok";
        }
        return res;
    }

    @Override
    public User update(UpdateRequestDTO request, String nickname) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUid(dao.findByNickname(nickname).get().getUid());
        user.setNickname(request.getNickname());
        user.setPassword(request.getPassword());
        user.setProfileImg(request.getProfileImg());
        user = dao.save(user);
        return user;
    }

    @Override
    public User select(String nickname) {
        return dao.findByNickname(nickname).orElse(null);
    }

    @Override
    public void withdraw(String nickname) {
        User du = dao.findByNickname(nickname).get();
        dao.deleteById(du.getUid());
    }

    @Override
    public User authentication(long aid, String token) {
        Optional<UserAuth> ua = authDao.findByAidAndToken(aid, token);
        if (ua.isPresent()) {
            UserAuth auth = ua.get();
            User user = new User();
            user.setEmail(auth.getEmail());
            user.setNickname(auth.getNickname());
            user.setPassword(auth.getPassword());
            authDao.delete(auth);
            user = dao.save(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public String findPw(String email, String nickname) {
        Optional<User> ou = dao.findByEmailAndNickname(email, nickname);
        String res = null;
        if (ou.isPresent()) {
            User u = ou.get();
            String temppw = "temp1234";
            u.setPassword(bcryptimpl.encrypt(temppw));
            System.out.println(u.getPassword());
            dao.save(u);
            
            //res = u.getPassword();
            res = temppw;
        } else {
            if (dao.findByEmail(email).isPresent()) {
                res = "nickname";
            } else {
                res = "email";
            }
        }

        return res;
    }

}
