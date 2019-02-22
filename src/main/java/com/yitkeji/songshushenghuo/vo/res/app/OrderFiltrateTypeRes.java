package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel
public class OrderFiltrateTypeRes {

    @ApiModelProperty("筛选类型")
    private List<TypeRes> typeList;

    @ApiModelProperty("筛选状态")
    private List<StatusRes> statusList;
}
