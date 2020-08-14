package com.blsa.ezilog.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    
    private RecommendPost calculate(Post original) {
        int replyCount = replyDao.countByPostNum(original.getNo());
        
        int likeScore = original.getLikeCount() * 13;
        int unlikeScore = original.getLikeCount() < original.getUnlikeCount() ? -original.getUnlikeCount() : original.getUnlikeCount();
        int replyScore = replyCount * 21;
        int viewScore = original.getViews();
        int baseScore = 500;
        
        RecommendPost recommend = new RecommendPost(
                original.getNo(),
                original.getWriter(), 
                original.getCategoryId(),
                original.getTitle(),
                original.getWriteDate(), 
                original.getLikeCount(), 
                original.getUnlikeCount(), 
                original.getViews(), 
                replyCount,
                baseScore + likeScore + unlikeScore + replyScore + viewScore
        );
        
        return recommend;
    }

    @Scheduled(fixedRate = 3600000)
    public void autoCalculate() {
        calculate();
    }

    public void calculate() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> list = postDao.findPostInSelection(pageable);
        
        Set<Long> map = new HashSet<>();
        for (Post x : list) {
            map.add(x.getNo());
        }
        
        recommendPosts = new ArrayList<>();
        Optional<List<Post>> optpost = postDao.findRecommandPostAllowed();
        List<Post> posts = new ArrayList<>();
        if (optpost.isPresent()) {
            posts = optpost.get();
            
            for (Post p : posts) {
                if (map.contains(p.getNo()))
                    continue;
                
                RecommendPost rp = calculate(p);
                recommendPosts.add(rp);
            }
            Collections.sort(recommendPosts);
            while (recommendPosts.size() > 5) {
                recommendPosts.remove(5);
            }
        } else {
            posts = null;
        }
    }

    public List<RecommendPost> getRecommendPosts() {
        return recommendPosts;
    }
}
