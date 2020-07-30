package com.blsa.ezilog.model.qna;

import java.math.BigInteger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class AnswerRequest {
    
    @ApiModelProperty(required = true)
    @NotNull
    String content;
    
    @ApiModelProperty(required = true)
    @NotNull
    String writer;
    
    @ApiModelProperty(required = true)
    @NotNull
    BigInteger qid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public BigInteger getQid() {
        return qid;
    }

    public void setQid(BigInteger qid) {
        this.qid = qid;
    }

    
    
    
}
