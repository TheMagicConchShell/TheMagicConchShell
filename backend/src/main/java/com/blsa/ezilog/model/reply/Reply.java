package com.blsa.ezilog.model.reply;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private BigInteger id;
    
    @Column
    private String writer;
    
    @Column(name = "post_no")
    private BigInteger postNo;
    
    @Column
    private String content;
    
    @Column (name = "write_date")
    LocalDateTime writeDate;
    
    @Column (name = "like_count")
    private int likeCount;
    
    @Column (name = "unlike_count")
    private int unlikeCount;
    
    @Column
    private boolean selected;
    
    @Column
    private boolean secret;
    
    @Transient
    private boolean isMine;
    
    @Transient
    private boolean isAuthor;

    public Reply(String writer, BigInteger postNo, String content, LocalDateTime writeDate, boolean secret) {
        super();
        this.writer = writer;
        this.postNo = postNo;
        this.content = content;
        this.writeDate = writeDate;
        this.secret = secret;
    }

   
}
