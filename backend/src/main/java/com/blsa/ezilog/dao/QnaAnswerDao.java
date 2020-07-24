package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.qna.QnaAnswer;

public interface QnaAnswerDao extends JpaRepository<QnaAnswer, Long> {
    Optional<QnaAnswer> getQnaAnswerByQid(BigInteger qid);
    Optional<QnaAnswer> getQnaAnswerByReplier(String replier);
    QnaAnswer getQnaAnswerByAid(BigInteger aid);
}
