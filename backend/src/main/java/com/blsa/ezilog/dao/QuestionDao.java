package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.qna.Question;

public interface QuestionDao extends JpaRepository<Question, Long> {
    Optional<Question> getQuestionByNo(Long qid);
    Optional<Question> getQuestionByWriter(String writer);
    Optional<Question> getQuestionByTitle(String title);
    
    Page<Question> findQuestionByWriter(String writer, Pageable request);
    Page<Question> findQuestionByTitleLike(String title, Pageable request);
    
    
}
