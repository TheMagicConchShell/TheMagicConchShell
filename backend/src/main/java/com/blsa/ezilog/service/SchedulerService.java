package com.blsa.ezilog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.PostDao;
import com.blsa.ezilog.dao.ReplyDao;
import com.blsa.ezilog.model.post.RecommendPost;

@Service
public class SchedulerService {

    private List<RecommendPost> recommendList;

    @Autowired
    private PostDao postDao;

    @Autowired
    private ReplyDao replyDao;

    @Scheduled(cron = "0 0/1 * * * *")
    private void autoRecommend() {

    }
}
