package com.blsa.ezilog.model.post;

import java.math.BigInteger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class PostCreateRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String title;
    
    @ApiModelProperty(required = true)
    @NotNull
    BigInteger categoryId;
    
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
    int views;
    
    @ApiModelProperty(required = true)
    @NotNull
    boolean allow;
    
    @ApiModelProperty(required = true)
    @NotNull
    boolean secret;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigInteger getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(BigInteger categoryId) {
        this.categoryId = categoryId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getUnlikeCount() {
        return unlikeCount;
    }

    public void setUnlikeCount(int unlikeCount) {
        this.unlikeCount = unlikeCount;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean getAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public boolean getSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    
    
    
}
