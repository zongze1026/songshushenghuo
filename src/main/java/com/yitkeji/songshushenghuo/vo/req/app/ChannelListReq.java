package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class ChannelListReq {

    @ApiModelProperty("银行卡ID")
    private Long cardId;


    @ApiModelProperty("业务类型")
    private BusinessType businessType;
}
