package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.qna.Answer;

public interface AnswerDao extends JpaRepository<Answer, Long> {
    Optional<Answer> getAnswerByQid(Long qid);
    Optional<Answer> getAnswerByWriter(String replier);
    Optional<Answer> getQnaAnswerByNo(Long aid);
    
    List<Answer> findAllByQid(Long qid);
}
