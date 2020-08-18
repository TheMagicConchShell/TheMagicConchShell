package com.blsa.ezilog.dao.rank;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blsa.ezilog.model.rank.UserRank;
import com.blsa.ezilog.model.reply.Reply;

public interface UserRankDao extends JpaRepository<UserRank, String> {
    @Query(value = "select t1.nickname as nickname, count(p.writer) as post_count, t1.rc as reply_count, t1.point as point,t1.level as level,t1.uid as uid\r\n" +
            "from (select u.uid, count(r.writer) as rc, nickname,point,level from user u left join reply r on u.nickname = r.writer group by u.uid) t1\r\n" +
            "left join post p on t1.nickname = p.writer\r\n" +
            "group by t1.nickname\r\n" +
            "order by ?1 desc, 6 desc\r\n" +
            "limit 10;", nativeQuery = true)
    List<UserRank> selectUserRankData(Integer sort);
    
}
