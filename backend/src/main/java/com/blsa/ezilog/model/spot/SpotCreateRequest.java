package com.blsa.ezilog.model.spot;

import javax.validation.Valid;

import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class SpotCreateRequest {

    @ApiModelProperty(required = true)
    @NotNull
    Long postNo;

    @ApiModelProperty(required = true)
    @NotNull
    String owner;

    public Long getPostNo() {
        return postNo;
    }

    public void setPostNo(Long postNo) {
        this.postNo = postNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
