package com.blsa.ezilog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.user.LoginRequestDTO;
import com.blsa.ezilog.model.user.SignupRequestDTO;
import com.blsa.ezilog.model.user.User;

@Service
public class UserServiceImpl implements UserService{

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
	public User login(LoginRequestDTO request) {
		return dao.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElse(null);
	}

	@Override
	public String duplicateCheck(String email, String nickname) {
		String res = null;
		if(dao.findByEmail(email).isPresent()) {
			res = "email";
		}else if(dao.findByNickname(nickname).isPresent()) {
			res = "nickname";
		}else {
			res = "ok";
		}
		return res;
		
	}
	
	
}
