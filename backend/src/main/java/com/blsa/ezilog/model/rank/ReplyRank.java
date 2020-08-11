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
public class ReplyRank {
    @Id
    private Long id;
    
    @Column
    private String writer;
    
    @Column(name = "post_no")
    private Long postNo;

    @Column (name = "write_date")
    LocalDateTime writeDate;
    
    @Column (name = "like_count")
    private int likeCount;
    
    @Column
    private boolean secret;
}
