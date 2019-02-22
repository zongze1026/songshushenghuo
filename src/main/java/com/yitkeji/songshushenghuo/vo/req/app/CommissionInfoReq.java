package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ApiModel
public class CommissionInfoReq extends BaseAppReq {

    @ApiModelProperty("分润记录ID")
    @NotNull
    private long commissionId;

}
