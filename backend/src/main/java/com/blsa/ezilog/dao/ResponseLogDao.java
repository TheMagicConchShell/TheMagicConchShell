package com.blsa.ezilog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.log.ResponseLog;

public interface ResponseLogDao extends JpaRepository<ResponseLog, Long>{

}
