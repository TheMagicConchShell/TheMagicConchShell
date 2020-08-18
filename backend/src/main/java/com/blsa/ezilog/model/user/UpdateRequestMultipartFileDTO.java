package com.blsa.ezilog.model.user;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
public class UpdateRequestMultipartFileDTO {
    @ApiModelProperty(required = false)
    String email;
    
    @ApiModelProperty(required = true)
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    String password;
    
    @ApiModelProperty(required = true)
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    String nickname;

    @ApiModelProperty(required = false)
    @Nullable
    MultipartFile profileImg;
}
