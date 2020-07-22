package com.blsa.ezilog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.user.User;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByUid(long uid);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
