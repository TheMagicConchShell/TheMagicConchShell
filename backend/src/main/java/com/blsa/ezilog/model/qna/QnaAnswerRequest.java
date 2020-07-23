package com.blsa.ezilog.model.qna;

import java.math.BigInteger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class QnaAnswerRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String qnaName;
    
    @ApiModelProperty(required = true)
    @NotNull
    String qnaReply;
    
    @ApiModelProperty(required = true)
    @NotNull
    String qnaReplier;
    
    @ApiModelProperty(required = true)
    @NotNull
    BigInteger qid;

    public String getQnaName() {
        return qnaName;
    }

    public void setQnaName(String qnaName) {
        this.qnaName = qnaName;
    }

    public String getQnaReply() {
        return qnaReply;
    }

    public void setQnaReply(String qnaReply) {
        this.qnaReply = qnaReply;
    }

    public String getQnaReplier() {
        return qnaReplier;
    }

    public void setQnaReplier(String qnaReplier) {
        this.qnaReplier = qnaReplier;
    }

    public BigInteger getQid() {
        return qid;
    }

    public void setQid(BigInteger qid) {
        this.qid = qid;
    }
    
    
}
