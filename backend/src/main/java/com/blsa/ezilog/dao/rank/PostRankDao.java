package com.blsa.ezilog.dao.rank;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.rank.PostRank;

public interface PostRankDao extends JpaRepository<PostRank, Long>{
    @Query(value= "select p.no as no, p.writer as writer, p.category_id as category_id, p.title as title, p.write_date as write_date,p.like_count as like_count, p.views as views, count(*) as reply_count,p.secret as secret\r\n" + 
            "from post p left join reply r on p.no=r.post_no\r\n" + 
            "group by p.no\r\n" + 
            "order by ?1 desc\r\n" + 
            "limit 10;",nativeQuery = true)
    List<PostRank> selectPostRankData(Integer sort);

}
