package com.blsa.ezilog.model;

import io.swagger.annotations.ApiModelProperty;

public class ErrorResponse extends BasicResponse {
    @ApiModelProperty(value = "errors", position = 3)
    public Object errors; // response-> errors
}