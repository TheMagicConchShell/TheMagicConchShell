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
public class QnaAnswer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="qna_aid")
    BigInteger aid;
    
    @Column(name ="qna_reply")
    String reply;
    
    @Column(name ="qna_replier")
    String replier;
    
    @Column (name="qna_qid")
    BigInteger qid;

    public QnaAnswer(String reply, String replier, BigInteger qid) {
        super();
        this.reply = reply;
        this.replier = replier;
        this.qid = qid;
    }
    
    
}
