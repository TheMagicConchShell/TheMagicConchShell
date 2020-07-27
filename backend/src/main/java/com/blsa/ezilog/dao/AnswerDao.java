package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.qna.Answer;

public interface AnswerDao extends JpaRepository<Answer, Long> {
    Optional<Answer> getAnswerByQid(BigInteger qid);
    Optional<Answer> getAnswerByWriter(String replier);
    Optional<Answer> getQnaAnswerByNo(BigInteger aid);
}
