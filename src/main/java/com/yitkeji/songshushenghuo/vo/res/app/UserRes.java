package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.vo.enums.Vip;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserRes{
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户推荐人手机号")
    private String referencePhone;

    @ApiModelProperty("用户代理手机号")
    private String agentPhone;

    @ApiModelProperty("用户真实姓名")
    private String realName;

    @ApiModelProperty("用户推荐人真实姓名")
    private String referenceRealName;

    @ApiModelProperty("用户代理真实姓名")
    private String agentRealName;

    @ApiModelProperty("用户VIP等级")
    private int vip;

    @ApiModelProperty("用户VIP等级描述")
    private String vipDesc;

    @ApiModelProperty("用户推荐人VIP等级")
    private int referenceVip;

    @ApiModelProperty("用户代理VIP等级")
    private int agentVip;

    @ApiModelProperty("认证状态")
    private Integer authStatus;

    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("推荐码")
    private String referralCode;

    @ApiModelProperty("提现卡ID")
    private Long debitCardId;

    @ApiModelProperty("余额")
    private int money = 0;

    @ApiModelProperty("直推好友数量")
    private long cdirectFriends;

    @ApiModelProperty("间推好友数量")
    private int cindirectFriends;

    @ApiModelProperty("直推已认证好友数量")
    private long cdirectFriendsAuthed;

    @ApiModelProperty("间推已认证好友数量")
    private int cindirectFriendsAuthed;

    @ApiModelProperty("真实姓名")
    private String targetAgentName;

    @ApiModelProperty("是否已绑卡")
    private boolean bindCard = false;

    @ApiModelProperty("今日分润总额")
    private int sumCommissionToday;

    @ApiModelProperty("昨日分润总额")
    private int sumCommissionYestoday;

    @ApiModelProperty("历史分润总额")
    private int sumCommissionAll;

    public String getVipDesc(){
        String vipDesc = Vip.getVip(getVip()).getDesc();
        return vipDesc;
    }
}
