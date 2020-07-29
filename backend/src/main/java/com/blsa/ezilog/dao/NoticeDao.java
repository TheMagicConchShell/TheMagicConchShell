package com.blsa.ezilog.dao;

import java.math.BigInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.notice.Notice;

public interface NoticeDao extends JpaRepository<Notice, Long> {
    @Query(value = "SELECT * FROM notice WHERE writer LIKE %:writer%",
            countQuery = "SELECT count(*) FROM notice WHERE writer LIKE %:writer%",
            nativeQuery = true)
    Page<Notice> findNoticeByWriter(String writer, Pageable request);

    Notice getNoticeByNid(BigInteger id);
}
