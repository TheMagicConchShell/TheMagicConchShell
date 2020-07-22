package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.notice.Notice;

public interface NoticeDao extends JpaRepository<Notice, String> {
    List<Notice> getNoticeByWriter(String writer) throws Exception;

    Notice getNoticeByNid(BigInteger id) throws Exception;
}
