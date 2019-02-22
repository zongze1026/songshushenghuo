package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class PayReq extends BaseAppReq{

    @ApiModelProperty("业务类型")
    private BusinessType businessType;

    @ApiModelProperty("VIP等级")
    private int vip;

    @ApiModelProperty("支付方式")
    private Channel channel;
}
