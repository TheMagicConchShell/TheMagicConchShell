package com.blsa.ezilog.model.qna;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Valid
@Data
@ToString
public class AnswerUpdateRequest {
    
    @ApiModelProperty(required = true)
    @NotNull
    Long no;
    
    @ApiModelProperty(required = true)
    @NotNull
    String content;
}
