package com.blsa.ezilog.model.post;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private BigInteger no;
    
    @Column (name = "writer")
    private String writer;
    
    @Column(name = "category_id")
    private BigInteger categoryId;
    
    @Column (name = "title")
    private String title;
    
    @Column(name = "content")
    private String content;
    
    @Column (name = "write_date")
    LocalDateTime writeDate;
    
    @Column(name ="like_count")
    private int likeCount;
    
    @Column(name ="unlike_count")
    private int unlikeCount;
    
    @Column (name = "views")
    private int views;
    
    @Column (name ="allow")
    private boolean allow;
    
    @Column (name ="secret")
    private boolean secret;

    public Post(String writer, BigInteger categoryId, String title, String content, LocalDateTime writeDate, int likeCount,
            int unlikeCount, int views, boolean allow, boolean secret) {
        super();
        this.writer = writer;
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
        this.likeCount = likeCount;
        this.unlikeCount = unlikeCount;
        this.views = views;
        this.allow = allow;
        this.secret = secret;
    }
    
    

    
    
    
    
}
