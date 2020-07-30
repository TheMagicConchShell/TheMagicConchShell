package com.blsa.ezilog.model.qna;

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
public class Answer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="no")
    BigInteger no;
    
    @Column(name ="content")
    String content;
    
    @Column(name ="writer")
    String writer;
    
    @Column (name="qid")
    BigInteger qid;
    
    @Column (name="write_date")
    LocalDateTime writeDate;

    public Answer(String content, String writer, BigInteger qid, LocalDateTime writeDate) {
        super();
        this.content = content;
        this.writer = writer;
        this.qid = qid;
        this.writeDate = writeDate;
    }

    
    
}
