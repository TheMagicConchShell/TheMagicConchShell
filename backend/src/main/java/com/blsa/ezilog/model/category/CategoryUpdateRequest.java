package com.blsa.ezilog.model.category;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class CategoryUpdateRequest {

    @ApiModelProperty(required = true)
    @NotNull
    String curName;

    @ApiModelProperty(required = true)
    @NotNull
    String changeName;

    @ApiModelProperty(required = true)
    @NotNull
    String description;

    @ApiModelProperty(required = true)
    @NotNull
    String enName;

    @ApiModelProperty(required = true)
    @NotNull
    String chName;

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

}
