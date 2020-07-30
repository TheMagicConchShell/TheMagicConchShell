package com.blsa.ezilog.model.like;

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
@AllArgsConstructor
@NoArgsConstructor
public class ReplyLikeCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger id;
    
    @Column(name="rely_id")
    BigInteger replyId;
    
    @Column
    BigInteger uid;
    
    @Column
    LocalDateTime stamp;
    
    @Column
    String type;

    public ReplyLikeCount(BigInteger replyId, BigInteger uid, LocalDateTime stamp, String type) {
        super();
        this.replyId = replyId;
        this.uid = uid;
        this.stamp = stamp;
        this.type = type;
    }
    
    
    
    
}
