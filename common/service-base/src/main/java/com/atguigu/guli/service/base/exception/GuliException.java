package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "全局异常")
public class GuliException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    public GuliException(ResultCodeEnum resultCodeEnum){

        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}

