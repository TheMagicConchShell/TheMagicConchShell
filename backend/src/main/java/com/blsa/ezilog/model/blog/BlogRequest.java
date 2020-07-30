package com.blsa.ezilog.model.blog;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class BlogRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String name;

    @ApiModelProperty(required = true)
    @NotNull
    String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
