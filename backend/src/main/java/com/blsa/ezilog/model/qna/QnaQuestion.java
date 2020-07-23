package com.blsa.ezilog.model.qna;

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
public class QnaQuestion {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "qna_qid")
    BigInteger qid;
    
    @Column(name = "qna_title")
    String title;
    
    @Column(name = "qna_content")
    String content;
    
    @Column(name = "qna_writer")
    String writer;

    public QnaQuestion(String title, String content, String writer) {
        super();
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
    
    
}
