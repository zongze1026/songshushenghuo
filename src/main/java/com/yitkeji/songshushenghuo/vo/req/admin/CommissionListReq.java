package com.yitkeji.songshushenghuo.vo.req.admin;

import com.yitkeji.songshushenghuo.vo.req.app.BaseAppPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CommissionListReq extends BaseAppPageReq {

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("vip等级")
    private Integer vip;

    @ApiModelProperty("统计月份，格式（年-月）")
    private String statisMonth;
}
