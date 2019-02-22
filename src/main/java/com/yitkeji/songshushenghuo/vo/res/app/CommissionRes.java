package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.CommissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class CommissionRes {

    @ApiModelProperty("分润ID")
    private Long commissionId;

    @ApiModelProperty("分润等级")
    private String level;

    @ApiModelProperty("分润类型")
    private Integer type;

    @ApiModelProperty("金额")
    private int money;

    @ApiModelProperty("统计月份")
    private String sumMonth;

    @ApiModelProperty("所属月份的收益总计")
    private Integer sumMonthMoney = 0;

    @ApiModelProperty("创建时间（Date）")
    private Date createTime;

    @ApiModelProperty("创建时间(字符串)")
    private String strCreateTime;

    @ApiModelProperty("类型图标")
    private String typeIcon;

    @ApiModelProperty("类型描述")
    private String typeDesc;

    public String getTypeDesc() {
        CommissionType commissionType = CommissionType.getType(getType());
        if(commissionType != null){
            return commissionType.getDesc();
        }
        return "未知类型";
    }

    public String getTypeIcon() {
        String staticDomain = SystemCfg.getInstance().getAppinfo().getStaticDomain();
        CommissionType commissionType = CommissionType.getType(getType());
        if(commissionType != null){
            return staticDomain + "/images/commissionTypeIcon" + getType() + ".png";
        }
        return staticDomain + "/images/commissionTypeIcon.png";
    }

    public String getStrCreateTime() {
        String strCreateTime =  DateUtil.format(getCreateTime(),DateUtil.DATEFORMAT_TWELVE);
        return  strCreateTime;
    }

    public String getSumMonth() {
        return DateUtil.format(createTime, "yyyy年MM月");
    }

    public Integer getSumMonthMoney() {
        return sumMonthMoney;
    }
}
