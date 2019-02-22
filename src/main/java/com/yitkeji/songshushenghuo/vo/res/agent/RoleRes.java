package com.yitkeji.songshushenghuo.vo.res.agent;

import com.yitkeji.songshushenghuo.vo.enums.RoleStatus;
import com.yitkeji.songshushenghuo.vo.enums.Sensitivity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class RoleRes {

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色内容")
    private String comment;

    @ApiModelProperty("角色状态")
    private RoleStatus status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date lastupdatetime;

    private String authority;

    private Sensitivity sensitivity;

    private Boolean readOnly;
}
