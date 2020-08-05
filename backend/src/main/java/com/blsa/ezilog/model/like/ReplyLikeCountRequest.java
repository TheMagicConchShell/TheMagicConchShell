package com.blsa.ezilog.model.like;

import java.math.BigInteger;

import javax.validation.Valid;

import lombok.ToString;

@Valid
@ToString
public class ReplyLikeCountRequest {
    
	Long replyId;
    
    String type;

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
