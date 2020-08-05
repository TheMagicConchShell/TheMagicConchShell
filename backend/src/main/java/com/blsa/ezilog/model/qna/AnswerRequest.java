package com.blsa.ezilog.model.qna;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Valid
@Data
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
    Long qid;
}
