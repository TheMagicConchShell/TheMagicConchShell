package com.blsa.ezilog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.user.User;

public interface UserDao extends JpaRepository<User, Long> {
	User findByUid(long uid);
}
