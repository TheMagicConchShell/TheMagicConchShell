package com.blsa.ezilog.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.PostDao;
import com.blsa.ezilog.dao.ReplyDao;
import com.blsa.ezilog.model.post.Post;
import com.blsa.ezilog.model.post.RecommendPost;

@Service
public class RecommendService {

    private List<RecommendPost> recommendPosts;

    @Autowired
    private PostDao postDao;

    @Autowired
    private ReplyDao replyDao;

    @Scheduled(fixedRate = 3600000)
    public void autoCalculate() {
        recommendPosts = new ArrayList<>();
        List<Post> posts = postDao.findAll();
        for (Post p : posts) {
            int replyCount = replyDao.countByPostNum(p.getNo());
            RecommendPost rp = new RecommendPost(p.getNo(), p.getWriter(), p.getCategoryId(), p.getTitle(),
                    p.getWriteDate(), p.getLikeCount(), p.getUnlikeCount(), p.getViews(), replyCount,
                    p.getLikeCount() - p.getUnlikeCount() + p.getViews() + replyCount);
            recommendPosts.add(rp);
        }
        Collections.sort(recommendPosts);
        while (recommendPosts.size() > 5) {
            recommendPosts.remove(5);
        }
    }

    public void calculate() {
        recommendPosts = new ArrayList<>();
        List<Post> posts = postDao.findAll();
        for (Post p : posts) {
            int replyCount = replyDao.countByPostNum(p.getNo());
            RecommendPost rp = new RecommendPost(p.getNo(), p.getWriter(), p.getCategoryId(), p.getTitle(),
                    p.getWriteDate(), p.getLikeCount(), p.getUnlikeCount(), p.getViews(), replyCount,
                    p.getLikeCount() - p.getUnlikeCount() + p.getViews() + replyCount);
            recommendPosts.add(rp);
        }
        Collections.sort(recommendPosts);
        while (recommendPosts.size() > 5) {
            recommendPosts.remove(5);
        }
    }

    public List<RecommendPost> getRecommendPosts() {
        return recommendPosts;
    }
}
