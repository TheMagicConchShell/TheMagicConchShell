package com.blsa.ezilog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.log.RequestLog;

public interface RequestLogDao extends JpaRepository<RequestLog, Long>{

    @Query(value = "Select * from request_log ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<RequestLog> getLastInsert();
}
