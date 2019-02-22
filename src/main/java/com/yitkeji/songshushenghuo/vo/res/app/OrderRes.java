package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.enums.OrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class OrderRes{

    @ApiModelProperty("账单ID")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

//    @ApiModelProperty("地区名称")
//    private String areaName;

    @ApiModelProperty("出账卡")
    private String sourceCardNo;

    @ApiModelProperty("出账卡银行名称")
    private String sourceCardBank;

    @ApiModelProperty("出账卡联行号")
    private String sourceUnionNum;

    @ApiModelProperty("入账卡")
    private String targetCardNo;

    @ApiModelProperty("入账卡银行名称")
    private String targetCardBank;

    @ApiModelProperty("入账卡联行号")
    private String targetUnionNum;

    @ApiModelProperty("订单日期（Date）")
    private Date createTime;

    @ApiModelProperty("订单日期（字符串）")
    private String strCreateTime;

    @ApiModelProperty("最后更新时间")
    private String strLastupdatetime;

    @ApiModelProperty("交易金额")
    private int money;

    @ApiModelProperty("手续费")
    private int rateMoney;

    @ApiModelProperty("到账金额")
    private int arriveMoney;

    @ApiModelProperty("账单状态")
    private String status;

    @ApiModelProperty("账单状态描述")
    private String statusDesc;

    @ApiModelProperty("账单类型")
    private int type;

    @ApiModelProperty("交易详情")
    private String comment;

    @ApiModelProperty("统计月份")
    private String sumMonth;

    @ApiModelProperty("所属月份的收益总计")
    private Integer sumMonthMoney = 0;

    @ApiModelProperty("账单类型图标")
    private String typeIcon;

    @ApiModelProperty("账单类型描述")
    private String typeDesc;

    public String getStrCreateTime() {
        if (strCreateTime == null){
            String strCreateTime = DateUtil.format(createTime, DateUtil.DATEFORMAT_NINTEEN);
            return strCreateTime;
        }
        return strCreateTime;
    }

    public String getStatusDesc() {
        OrderStatus orderStatus = OrderStatus.getStatus(getStatus());
        if(orderStatus != null){
            return getTypeDesc() + orderStatus.getDesc();
        }
        return "未知状态";
    }

    public String getTypeDesc() {
        OrderType orderType = OrderType.getType(getType());
        if(orderType != null){
            return orderType.getDesc();
        }
        return "未知类型";
    }

    public String getTypeIcon() {
        String staticDomain = SystemCfg.getInstance().getAppinfo().getStaticDomain();
        if(this.getOrderId() != null){
            return staticDomain + "/images/orderTypeIcon" + getType() + ".png";
        }
        return staticDomain + "/images/orderTypeIcon.png";
    }

    public String getSumMonth() {
        return DateUtil.format(createTime, "yyyy年MM月");
    }

    public Integer getSumMonthMoney() {
        return sumMonthMoney;
    }
}
