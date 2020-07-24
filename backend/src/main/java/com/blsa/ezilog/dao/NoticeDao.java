package com.blsa.ezilog.dao;

import java.math.BigInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.notice.Notice;

public interface NoticeDao extends JpaRepository<Notice, Long> {
    Page<Notice> findNoticeByWriter(String writer, Pageable request) throws Exception;

    Notice getNoticeByNid(BigInteger id) throws Exception;
}
