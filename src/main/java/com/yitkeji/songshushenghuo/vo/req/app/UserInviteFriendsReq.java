package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.enums.AuthStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserInviteFriendsReq extends BaseAppPageReq {

    @ApiModelProperty("认证状态(SUCCESS.已認證)")
    private AuthStatus authStatus;
}
