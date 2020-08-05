package com.blsa.ezilog.model.notice;

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

public class Notice {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long nid;
	
    @Column (name = "title")
    private String title;
	
    @Column (name = "content")
    private String content;
	
    @Column (name = "writer")
    private String writer;

    @Column (name = "write_date")
    LocalDateTime writeDate;

    public Notice(String title, String content, String writer, LocalDateTime writeDate) {
        super();
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writeDate = writeDate;
    }
    
}
