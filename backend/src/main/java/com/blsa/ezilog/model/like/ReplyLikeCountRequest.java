package com.blsa.ezilog.model.like;

import java.math.BigInteger;

import javax.validation.Valid;

import lombok.ToString;

@Valid
@ToString
public class ReplyLikeCountRequest {
    
    BigInteger replyId;
    
    String type;

    public BigInteger getReplyId() {
        return replyId;
    }

    public void setReplyId(BigInteger replyId) {
        this.replyId = replyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
