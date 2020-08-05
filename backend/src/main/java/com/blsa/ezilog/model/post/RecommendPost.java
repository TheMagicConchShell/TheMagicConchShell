package com.blsa.ezilog.model.post;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendPost implements Comparable<RecommendPost> {
    private Long no;

    private String writer;

    private Long categoryId;

    private String title;

    LocalDateTime writeDate;

    private int likeCount;

    private int unlikeCount;

    private int views;

    private int replyCount;

    private int totalScore;

    @Override
    public int compareTo(RecommendPost o) {
        if (this.totalScore > o.totalScore) {
            return -1;
        } else if (this.totalScore < o.totalScore) {
            return 1;
        } else {
            if (this.writeDate.isBefore(o.writeDate)) {
                return -1;
            } else if (this.writeDate.isAfter(o.writeDate)) {
                return 1;
            }
            return 0;
        }
    }

}
