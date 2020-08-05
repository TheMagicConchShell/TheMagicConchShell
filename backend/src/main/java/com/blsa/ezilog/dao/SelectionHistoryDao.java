package com.blsa.ezilog.dao;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.post.SelectionHistory;

public interface SelectionHistoryDao extends JpaRepository<SelectionHistory, Long>{

}
