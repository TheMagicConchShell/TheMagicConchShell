package com.blsa.ezilog.dao.rank;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.rank.ReplyRank;

public interface ReplyRankDao extends JpaRepository<ReplyRank, Long>{
    @Query (value = "select id,writer,post_no,write_date,like_count,secret\r\n" + 
            "from reply\r\n" + 
            "order by like_count desc, id desc\r\n" +
            "limit 10;",nativeQuery = true)
    List<ReplyRank> selectReplyRankData();
}
