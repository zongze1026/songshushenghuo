package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.Idcard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class UserCheckIdcardReq {

    @ApiModelProperty("身份证号")
    private String idcard;

    @Idcard
    public String getIdcard() {
        return idcard;
    }
}
