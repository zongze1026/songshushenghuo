package com.yitkeji.songshushenghuo.vo.req.admin;

import com.yitkeji.songshushenghuo.vo.enums.RoleStatus;
import com.yitkeji.songshushenghuo.vo.enums.Sensitivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleAddReq {

    @ApiModelProperty("名称")
    private String roleName;

    @ApiModelProperty("内容")
    private String comment;

    @ApiModelProperty("状态")
    private RoleStatus status;

    @ApiModelProperty("权限")
    private String authority;

    @ApiModelProperty("敏感度")
    private Sensitivity sensitivity;

    @ApiModelProperty("是否显示")
    private Boolean readOnly;
}
