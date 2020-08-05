package com.blsa.ezilog.model.qna;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class QuestionUpdateRequest {
    
    @ApiModelProperty(required = true)
    @NotNull
    Long no;
    
    @ApiModelProperty(required = true)
    @NotNull
    String title;
    
    @ApiModelProperty(required = true)
    @NotNull
    String content;
    
    @ApiModelProperty(required = true)
    @NotNull
    String writer;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
    
    
}
