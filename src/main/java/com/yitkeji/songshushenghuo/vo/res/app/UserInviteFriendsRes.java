package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.vo.enums.Vip;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class UserInviteFriendsRes {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("会员等级")
    private int vip;

    @ApiModelProperty("用户VIP等级描述")
    private String vipDesc;

    @ApiModelProperty("创建时间（Date類型）")
    private Date createTime;

    @ApiModelProperty("创建时间（字符串類型）")
    private String strCreateTime;

    public String getStrCreateTime() {
        String strCreateTime =  DateUtil.format(getCreateTime(),DateUtil.DATEFORMAT_TWELVE);
        return  strCreateTime;
    }

    public String getVipDesc(){
        String vipDesc = Vip.getVip(getVip()).getDesc();
        return vipDesc;
    }
}
