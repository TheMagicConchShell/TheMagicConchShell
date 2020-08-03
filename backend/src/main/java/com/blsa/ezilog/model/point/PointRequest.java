package com.blsa.ezilog.model.point;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class PointRequest {
    @ApiModelProperty(required = true)
    @NotNull
    int change;
    
    @ApiModelProperty(required = true)
    @NotNull
    String reason;

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
    
}
