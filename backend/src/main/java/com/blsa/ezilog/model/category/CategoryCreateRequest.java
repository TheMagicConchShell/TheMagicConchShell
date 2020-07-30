package com.blsa.ezilog.model.category;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class CategoryCreateRequest {

    @ApiModelProperty(required = true)
    @NotNull
    String name;

    @ApiModelProperty(required = true)
    @NotNull
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
