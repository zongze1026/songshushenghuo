package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CountUserRes {

    @ApiModelProperty("日期")
    private String countDate;

    @ApiModelProperty("用户数")
    private int countUser;
}
