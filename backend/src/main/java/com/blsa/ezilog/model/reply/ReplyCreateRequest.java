package com.blsa.ezilog.model.reply;

import java.math.BigInteger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class ReplyCreateRequest {
    
    @ApiModelProperty(required = true)
    @NotNull
    BigInteger postNo;
    
    @ApiModelProperty(required = true)
    @NotNull 
    String writer;
    
    @ApiModelProperty(required = true)
    @NotNull
    String content;
    
   
    @ApiModelProperty(required = true)
    @NotNull 
    int likeCount;
    
    @ApiModelProperty(required = true)
    @NotNull
    int unlikeCount;
    
    @ApiModelProperty(required = true)
    @NotNull
    boolean selected;
    
    @ApiModelProperty(required = true)
    @NotNull
    boolean secret;

    public BigInteger getPostNo() {
        return postNo;
    }

    public void setPostNo(BigInteger postNo) {
        this.postNo = postNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLike(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getUnlikeCount() {
        return unlikeCount;
    }

    public void setUnlike(int unlikeCount) {
        this.unlikeCount = unlikeCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
    
}
