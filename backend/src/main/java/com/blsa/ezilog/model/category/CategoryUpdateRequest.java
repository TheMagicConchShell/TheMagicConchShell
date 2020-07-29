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
    String changName;


    @ApiModelProperty(required = true)
    @NotNull
    String description;


    public String getCurName() {
        return curName;
    }


    public void setCurName(String curName) {
        this.curName = curName;
    }


    public String getChangName() {
        return changName;
    }


    public void setChangName(String changName) {
        this.changName = changName;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

   
   

}
