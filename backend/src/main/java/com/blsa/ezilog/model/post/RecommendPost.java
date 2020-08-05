package com.blsa.ezilog.model.post;

import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendPost {
    private Long no;

    private String writer;

    private Long categoryId;

    private String title;

    private String content;

    private LocalDateTime writeDate;

    private int likeCount;

    private int unlikeCount;

    private int views;

    private int replyCount;

    private int total;
}
