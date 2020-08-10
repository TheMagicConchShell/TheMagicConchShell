package com.blsa.ezilog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.user.Sns;

public interface SnsDao extends JpaRepository<Sns, Long> {
    Optional<Sns> findBySnsId(String snsId);
}
