package com.blsa.ezilog.model.notice;

import java.math.BigInteger;

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
    private BigInteger nid;
	
    @Column (name = "title")
    private String title;
	
    @Column (name = "content")
    private String content;
	
    @Column (name = "writer")
    private String writer;

    public Notice(String title, String content, String writer) {
        super();
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
