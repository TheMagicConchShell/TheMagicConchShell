package com.blsa.ezilog.model.post;

import java.math.BigInteger;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
public class SelectionPostRequestDTO {
    @ApiModelProperty(required = true)
    @NotNull
    Long no;
    
    @ApiModelProperty(required = true)
    @NotBlank(message = "설명은 필수 입력값입니다.")
    String description;
}
