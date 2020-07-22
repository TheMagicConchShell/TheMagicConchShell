package com.blsa.ezilog.model;

import io.swagger.annotations.ApiModelProperty;

public class BasicResponse {
    @ApiModelProperty(value = "status", position = 1)
    public String status; // 응답? 코드? HttpStatus에서 확장되는 에러 코드?????? HTTPSTATUSCODE e4001 s200 s201
    @ApiModelProperty(value = "message", position = 2)
    public String message;
    @ApiModelProperty(value = "data", position = 3)
    public Object data; // response-> data
}