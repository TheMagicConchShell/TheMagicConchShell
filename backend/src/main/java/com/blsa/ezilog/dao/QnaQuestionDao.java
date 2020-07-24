package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.qna.QnaQuestion;

public interface QnaQuestionDao extends JpaRepository<QnaQuestion, Long> {
    Optional<QnaQuestion> getQnaQuestionByQid(BigInteger qid);
    Optional<QnaQuestion> getQnaQuestionByWriter(String writer);
    Optional<QnaQuestion> getQnaQuestionByTitle(String title);
    
}
