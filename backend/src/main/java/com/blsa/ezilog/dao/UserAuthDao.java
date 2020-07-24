package com.blsa.ezilog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.user.UserAuth;

public interface UserAuthDao extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByUaidAndToken(long uaid, String token);

    Optional<UserAuth> findByEmail(String email);

    Optional<UserAuth> findByNickname(String nickname);
}
