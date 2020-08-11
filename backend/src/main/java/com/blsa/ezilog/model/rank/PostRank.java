package com.blsa.ezilog.model.rank;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRank {
    @Id
    private Long no;
    
    @Column (name = "writer")
    private String writer;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column (name = "title")
    private String title;

    @Column (name = "write_date")
    LocalDateTime writeDate;
    
    @Column(name ="like_count")
    private int likeCount;
    
    @Column (name = "views")
    private int views;
    
    @Column (name = "reply_count")
    private int replyCount;
    
    @Column (name ="secret")
    private boolean secret;
    
}
