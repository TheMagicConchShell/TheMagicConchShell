package com.blsa.ezilog.model.blog;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class BlogUpdateRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String name;

    @ApiModelProperty(required = true)
    @NotNull
    BigInteger id;
    
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

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

}
