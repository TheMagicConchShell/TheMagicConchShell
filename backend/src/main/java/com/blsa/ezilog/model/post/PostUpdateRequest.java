package com.blsa.ezilog.model.post;

import java.math.BigInteger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class PostUpdateRequest {
    
    @ApiModelProperty(required = true)
    @NotNull
    Long no;
    
    @ApiModelProperty(required = true)
    @NotNull
    String title;
    
    @ApiModelProperty(required = true)
    @NotNull
    Long categoryId;
    
    @ApiModelProperty(required = true)
    @NotNull
    String content;
    
    
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    
    
    
}
